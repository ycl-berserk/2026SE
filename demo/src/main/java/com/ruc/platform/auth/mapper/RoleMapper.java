package com.ruc.platform.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.auth.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT * FROM t_role WHERE role_code = #{roleCode} LIMIT 1")
    Role selectByRoleCode(@Param("roleCode") String roleCode);
}
