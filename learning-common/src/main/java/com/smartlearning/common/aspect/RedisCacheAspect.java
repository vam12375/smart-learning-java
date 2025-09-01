package com.smartlearning.common.aspect;

import com.smartlearning.common.annotation.RedisCache;
import com.smartlearning.common.annotation.RedisEvict;
import com.smartlearning.common.utils.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Redis缓存切面
 */
@Aspect
@Component
public class RedisCacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);

    @Autowired
    private RedisUtil redisUtil;

    private SpelExpressionParser parser = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 缓存切面
     */
    @Around("@annotation(redisCache)")
    public Object cache(ProceedingJoinPoint joinPoint, RedisCache redisCache) throws Throwable {
        // 解析SpEL表达式生成key
        String key = generateKey(joinPoint, redisCache.key(), redisCache.prefix());
        
        try {
            // 先从缓存中获取数据
            Object cacheResult = redisUtil.get(key);
            if (cacheResult != null) {
                logger.debug("Cache hit: {}", key);
                return cacheResult;
            }
            
            // 缓存未命中，执行方法
            Object result = joinPoint.proceed();
            
            // 将结果放入缓存
            if (result != null) {
                redisUtil.set(key, result, redisCache.timeout());
                logger.debug("Cache put: {}", key);
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Cache operation failed for key: {}", key, e);
            // 缓存操作失败，直接执行方法
            return joinPoint.proceed();
        }
    }

    /**
     * 缓存清除切面
     */
    @Around("@annotation(redisEvict)")
    public Object evict(ProceedingJoinPoint joinPoint, RedisEvict redisEvict) throws Throwable {
        try {
            // 先执行方法
            Object result = joinPoint.proceed();
            
            // 清除缓存
            if (redisEvict.allEntries()) {
                // 清除所有以prefix开头的缓存
                String pattern = redisEvict.prefix() + "*";
                logger.debug("Cache evict all: {}", pattern);
                // 这里可以实现批量删除逻辑
            } else {
                // 清除指定key的缓存
                String key = generateKey(joinPoint, redisEvict.key(), redisEvict.prefix());
                redisUtil.del(key);
                logger.debug("Cache evict: {}", key);
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Cache evict operation failed", e);
            throw e;
        }
    }

    /**
     * 生成缓存key
     */
    private String generateKey(ProceedingJoinPoint joinPoint, String keyExpression, String prefix) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取方法参数名
        String[] paramNames = discoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();
        
        // 创建SpEL解析上下文
        EvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        
        // 解析表达式
        Expression expression = parser.parseExpression(keyExpression);
        String key = expression.getValue(context, String.class);
        
        // 添加前缀
        if (prefix != null && !prefix.isEmpty()) {
            key = prefix + ":" + key;
        }
        
        return key;
    }
}