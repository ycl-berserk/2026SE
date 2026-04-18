package com.ruc.platform.leave.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 请假申请实体（复用 certificate_application 表）
 */
@Data
@TableName("certificate_application")
public class LeaveApplication {

    @TableId
    private Long id;

    private Long userId;

    private String typeCode;

    private String title;

    private String reason;

    private Long fileId;

    private Integer status;

    private LocalDateTime submitTime;

    private Long approvedBy;

    private LocalDateTime approvedAt;

    private String rejectReason;

    private Long certificateFileId;

    private LocalDate leaveStartDate;

    private LocalDate leaveEndDate;

    private String contactPhone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
