package com.ruc.platform.party.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.party.entity.PartyReminder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 党团提醒Mapper
 */
@Mapper
public interface PartyReminderMapper extends BaseMapper<PartyReminder> {

    @Select("SELECT * FROM party_reminder WHERE user_id = #{userId} AND status = 0 ORDER BY deadline ASC")
    List<PartyReminder> selectPendingByUserId(@Param("userId") Long userId);
}
