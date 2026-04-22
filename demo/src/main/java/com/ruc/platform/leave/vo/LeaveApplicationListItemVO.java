package com.ruc.platform.leave.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 请假列表项 VO
 */
@Data
public class LeaveApplicationListItemVO {

    private Long id;

    private Long userId;

    private String applicantName;

    private String applicantStudentNo;

    private String title;

    private String reason;

    private Integer status;

    private String statusText;

    private LocalDate leaveStartDate;

    private LocalDate leaveEndDate;

    private LocalDateTime submitTime;
}
