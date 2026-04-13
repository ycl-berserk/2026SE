package com.ruc.platform.party.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.party.entity.PartyStudentProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 学生党团进度Mapper
 */
@Mapper
public interface PartyStudentProgressMapper extends BaseMapper<PartyStudentProgress> {

    @Select("SELECT * FROM party_student_progress WHERE user_id = #{userId}")
    PartyStudentProgress selectByUserId(@Param("userId") Long userId);
}
