package com.ruc.platform.notice.service;

import com.ruc.platform.notice.vo.MessageVO;

import java.util.List;

/**
 * 消息服务接口
 */
public interface MessageService {

    /**
     * 获取最近消息
     */
    List<MessageVO> getRecentMessages(Long userId, Integer limit);

    /**
     * 获取未读消息数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 标记消息为已读
     */
    void markAsRead(Long messageId);
}
