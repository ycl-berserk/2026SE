package com.ruc.platform.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.notice.entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户消息Mapper
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    @Select("SELECT * FROM user_message WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit}")
    List<UserMessage> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    @Select("SELECT COUNT(*) FROM user_message WHERE user_id = #{userId} AND read_status = 0")
    Long countUnreadByUserId(@Param("userId") Long userId);

    @Update("UPDATE user_message SET read_status = 1, read_time = NOW() WHERE id = #{id}")
    int markAsRead(@Param("id") Long id);
}
