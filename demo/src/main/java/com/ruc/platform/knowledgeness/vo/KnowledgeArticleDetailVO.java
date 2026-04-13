package com.ruc.platform.knowledgeness.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识条目详情VO
 */
@Data
public class KnowledgeArticleDetailVO {

    /**
     * 条目ID
     */
    private Long id;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 详细内容
     */
    private String content;

    /**
     * 标准答案/办理建议
     */
    private String answer;

    /**
     * 来源
     */
    private String source;

    /**
     * 关键词列表
     */
    private List<String> keywords;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 浏览次数
     */
    private Long viewCount;
}
