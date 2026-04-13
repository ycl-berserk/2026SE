package com.ruc.platform.party.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 党团记录VO
 */
@Data
public class PartyRecordVO {

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 阶段编码
     */
    private String stageCode;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 记录类型
     */
    private String recordType;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;
}
