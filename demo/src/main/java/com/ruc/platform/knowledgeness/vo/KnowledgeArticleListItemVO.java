package com.ruc.platform.knowledgeness.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识条目列表项VO
 */
@Data
public class KnowledgeArticleListItemVO {

    /**
     * 条目ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 浏览次数
     */
    private Long viewCount;
}
