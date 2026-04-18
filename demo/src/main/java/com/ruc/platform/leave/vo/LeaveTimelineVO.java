package com.ruc.platform.leave.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 请假审批时间线 VO
 */
@Data
public class LeaveTimelineVO {

    private String action;

    private String actionText;

    private String operatorName;

    private String comment;

    private LocalDateTime createdAt;
}
