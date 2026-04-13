package com.ruc.platform.party.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 党团提醒实体
 */
@Data
@TableName("party_reminder")
public class PartyReminder {

    @TableId
    private Long id;

    private Long userId;

    private String title;

    private String content;

    private LocalDateTime deadline;

    private Integer status;

    private String reminderType;

    private LocalDateTime createdAt;
}
