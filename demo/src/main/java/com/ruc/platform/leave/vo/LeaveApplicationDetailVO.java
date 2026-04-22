package com.ruc.platform.leave.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 请假详情 VO
 */
@Data
public class LeaveApplicationDetailVO {

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

    private String contactPhone;

    private Long fileId;

    private String rejectReason;

    private LocalDateTime submitTime;

    private String approvedByName;

    private LocalDateTime approvedAt;

    private List<LeaveTimelineVO> timelines;
}
