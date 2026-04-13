package com.ruc.platform.notice.service;

import com.ruc.platform.notice.entity.UserMessage;
import com.ruc.platform.notice.mapper.UserMessageMapper;
import com.ruc.platform.notice.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserMessageMapper userMessageMapper;

    @Override
    public List<MessageVO> getRecentMessages(Long userId, Integer limit) {
        List<UserMessage> messages = userMessageMapper.selectRecentByUserId(userId, limit);
        return messages.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return userMessageMapper.countUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long messageId) {
        userMessageMapper.markAsRead(messageId);
        log.info("标记消息为已读，messageId: {}", messageId);
    }

    private MessageVO convertToVO(UserMessage message) {
        MessageVO vo = new MessageVO();
        BeanUtils.copyProperties(message, vo);
        return vo;
    }
}
