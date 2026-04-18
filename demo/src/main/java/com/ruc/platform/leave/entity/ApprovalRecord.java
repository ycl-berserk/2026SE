package com.ruc.platform.leave.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批记录实体
 */
@Data
@TableName("approval_record")
public class ApprovalRecord {

    @TableId
    private Long id;

    private Long applicationId;

    private Long approverId;

    private String action;

    private String comment;

    private LocalDateTime createdAt;
}
