package com.ruc.platform.notice.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息VO
 */
@Data
public class MessageVO {

    private Long id;

    private Long noticeId;

    private String title;

    private String summary;

    private Integer readStatus;

    private LocalDateTime readTime;

    private LocalDateTime createdAt;
}
