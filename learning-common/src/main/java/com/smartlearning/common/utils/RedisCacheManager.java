package com.smartlearning.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Redis缓存管理工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取缓存统计信息
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 获取所有key的数量
            Set<String> keys = redisTemplate.keys("*");
            stats.put("totalKeys", keys != null ? keys.size() : 0);
            
            // 按前缀分组统计
            Map<String, Integer> prefixStats = new HashMap<>();
            if (keys != null) {
                for (String key : keys) {
                    String prefix = extractPrefix(key);
                    prefixStats.put(prefix, prefixStats.getOrDefault(prefix, 0) + 1);
                }
            }
            stats.put("prefixStats", prefixStats);
            
            log.info("Redis缓存统计: {}", stats);
            return stats;
        } catch (Exception e) {
            log.error("获取缓存统计失败", e);
            stats.put("error", e.getMessage());
            return stats;
        }
    }

    /**
     * 清除指定前缀的所有缓存
     */
    public void clearCacheByPrefix(String prefix) {
        try {
            Set<String> keys = redisTemplate.keys(prefix + "*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("清除缓存成功: prefix={}, count={}", prefix, keys.size());
            }
        } catch (Exception e) {
            log.error("清除缓存失败: prefix={}", prefix, e);
        }
    }

    /**
     * 清除所有缓存
     */
    public void clearAllCache() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("清除所有缓存成功: count={}", keys.size());
            }
        } catch (Exception e) {
            log.error("清除所有缓存失败", e);
        }
    }

    /**
     * 检查缓存是否存在
     */
    public boolean exists(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("检查缓存存在性失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 获取缓存剩余过期时间（秒）
     */
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            log.error("获取缓存过期时间失败: key={}", key, e);
            return -1;
        }
    }

    /**
     * 提取key的前缀
     */
    private String extractPrefix(String key) {
        int colonIndex = key.indexOf(':');
        if (colonIndex > 0) {
            return key.substring(0, colonIndex);
        }
        return "other";
    }
}