package com.ruc.platform.party.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.party.entity.PartyStudentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 党团记录Mapper
 */
@Mapper
public interface PartyStudentRecordMapper extends BaseMapper<PartyStudentRecord> {

    @Select("SELECT * FROM party_student_record WHERE user_id = #{userId} ORDER BY event_time DESC")
    List<PartyStudentRecord> selectByUserId(@Param("userId") Long userId);
}
