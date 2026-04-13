package com.ruc.platform.party.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 党团概览VO
 */
@Data
public class PartyOverviewVO {

    /**
     * 当前阶段编码
     */
    private String currentStageCode;

    /**
     * 当前阶段名称
     */
    private String currentStageName;

    /**
     * 阶段进度百分比
     */
    private Integer progress;

    /**
     * 待处理提醒事项数量
     */
    private Integer pendingReminders;

    /**
     * 最近一次提交时间
     */
    private LocalDateTime lastSubmitTime;
}
