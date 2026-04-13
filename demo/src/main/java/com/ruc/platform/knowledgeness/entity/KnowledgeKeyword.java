package com.ruc.platform.knowledgeness.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识关键词实体类
 * 对应数据库表：knowledge_keyword
 */
@Data
@TableName("knowledge_keyword")
public class KnowledgeKeyword {

    /**
     * 关键词ID（主键）
     */
    @TableId
    private Long id;

    /**
     * 条目ID
     */
    private Long articleId;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
