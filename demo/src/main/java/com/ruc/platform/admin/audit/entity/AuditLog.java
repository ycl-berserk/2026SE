package com.ruc.platform.admin.audit.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("audit_log")
public class AuditLog {

    @TableId
    private Long id;

    private Long userId;

    private String module;

    private String action;

    private String description;

    private String requestMethod;

    private String requestUrl;

    private String ipAddress;

    private String userAgent;

    private Long executionTime;

    private Integer status;

    private String errorMessage;

    private LocalDateTime createdAt;
}
