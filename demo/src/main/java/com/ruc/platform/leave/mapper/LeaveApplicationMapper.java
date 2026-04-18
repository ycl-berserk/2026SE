package com.ruc.platform.leave.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.leave.entity.LeaveApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 请假申请 Mapper
 */
@Mapper
public interface LeaveApplicationMapper extends BaseMapper<LeaveApplication> {

    @Select("""
            SELECT *
            FROM certificate_application
            WHERE user_id = #{userId}
              AND type_code = 'leave'
            ORDER BY submit_time DESC
            """)
    List<LeaveApplication> selectLeaveByUserId(@Param("userId") Long userId);
}
