package com.ruc.platform.leave.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.leave.entity.ApprovalRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 审批记录 Mapper
 */
@Mapper
public interface ApprovalRecordMapper extends BaseMapper<ApprovalRecord> {

    @Select("""
            SELECT *
            FROM approval_record
            WHERE application_id = #{applicationId}
            ORDER BY created_at ASC
            """)
    List<ApprovalRecord> selectByApplicationId(@Param("applicationId") Long applicationId);
}
