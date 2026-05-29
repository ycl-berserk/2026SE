package com.ruc.platform.admin.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruc.platform.admin.audit.dto.AuditLogQueryDTO;
import com.ruc.platform.admin.audit.entity.AuditLog;
import com.ruc.platform.admin.audit.vo.AuditLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditLogMapper extends BaseMapper<AuditLog> {

    List<AuditLogVO> selectPage(
            IPage<?> page,
            @Param("q") AuditLogQueryDTO query
    );

    Long count(@Param("q") AuditLogQueryDTO query);

    int deleteOlderThanLimit(@Param("maxRows") int maxRows);
}
