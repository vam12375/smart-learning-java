package com.smartlearning.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 */
@Data
public class PageResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 数据列表
     */
    private List<T> list;
    
    /**
     * 分页信息
     */
    private Pagination pagination;
    
    public PageResult() {}
    
    public PageResult(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }
    
    public PageResult(List<T> list, long page, long size, long total) {
        this.list = list;
        this.pagination = new Pagination(page, size, total);
    }
    
    /**
     * 分页信息内部类
     */
    @Data
    public static class Pagination implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        /**
         * 当前页码
         */
        private Long page;
        
        /**
         * 每页大小
         */
        private Long size;
        
        /**
         * 总记录数
         */
        private Long total;
        
        /**
         * 总页数
         */
        private Long pages;
        
        /**
         * 是否有上一页
         */
        private Boolean hasPrevious;
        
        /**
         * 是否有下一页
         */
        private Boolean hasNext;
        
        public Pagination() {}
        
        public Pagination(long page, long size, long total) {
            this.page = page;
            this.size = size;
            this.total = total;
            this.pages = (total + size - 1) / size; // 向上取整
            this.hasPrevious = page > 1;
            this.hasNext = page < this.pages;
        }
    }
}
