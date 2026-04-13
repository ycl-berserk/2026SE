package com.ruc.platform.home.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 最新通知VO
 */
@Data
public class LatestNoticeVO {

    private Long id;

    private String title;

    private String summary;

    private LocalDateTime publishTime;

    private String noticeType;
}
