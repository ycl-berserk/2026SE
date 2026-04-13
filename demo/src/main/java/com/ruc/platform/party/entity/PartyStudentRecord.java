package com.ruc.platform.party.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 党团记录实体
 */
@Data
@TableName("party_student_record")
public class PartyStudentRecord {

    @TableId
    private Long id;

    private Long userId;

    private String stageCode;

    private String recordType;

    private String title;

    private String description;

    private LocalDateTime eventTime;

    private Integer status;

    private LocalDateTime createdAt;
}
