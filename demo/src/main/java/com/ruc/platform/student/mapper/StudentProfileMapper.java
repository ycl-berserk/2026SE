package com.ruc.platform.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.student.entity.StudentProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 学生档案Mapper接口
 */
@Mapper
public interface StudentProfileMapper extends BaseMapper<StudentProfile> {

    /**
     * 根据用户ID查询学生档案
     * @param userId 用户ID
     * @return 学生档案实体
     */
    @Select("SELECT * FROM student_profile WHERE user_id = #{userId}")
    StudentProfile selectByUserId(@Param("userId") Long userId);

    /**
     * 根据学号查询学生档案
     * @param studentNo 学号
     * @return 学生档案实体
     */
    @Select("SELECT * FROM student_profile WHERE student_no = #{studentNo}")
    StudentProfile selectByStudentNo(@Param("studentNo") String studentNo);
}
