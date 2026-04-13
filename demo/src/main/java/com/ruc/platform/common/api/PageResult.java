package com.ruc.platform.common.api;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果封装
 * @param <T> 分页数据类型
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 每页大小
     */
    private Long pageSize;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private List<T> records;

    public PageResult() {
    }

    public PageResult(Long total, Long pageNum, Long pageSize, Long pages, List<T> records) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = pages;
        this.records = records;
    }

    /**
     * 便捷创建分页结果
     */
    public static <T> PageResult<T> of(Long total, Long pageNum, Long pageSize, List<T> records) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setRecords(records);
        pageResult.setPages((total + pageSize - 1) / pageSize);
        return pageResult;
    }
}
