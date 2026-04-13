package com.ruc.platform.party.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 党团提醒VO
 */
@Data
public class PartyReminderVO {

    /**
     * 提醒ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 截止时间
     */
    private LocalDateTime deadline;

    /**
     * 状态：0-待处理，1-已完成，2-已过期
     */
    private Integer status;

    /**
     * 提醒类型
     */
    private String reminderType;
}
