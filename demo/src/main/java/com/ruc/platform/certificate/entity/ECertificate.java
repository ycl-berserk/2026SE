package com.ruc.platform.certificate.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("e_certificate")
public class ECertificate {

    @TableId
    private Long id;

    private Long userId;

    private String title;

    private String reason;

    private String templateType;

    private Integer status;

    private LocalDateTime submitTime;

    private Long approvedBy;

    private LocalDateTime approvedAt;

    private String rejectReason;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long certificateFileId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
