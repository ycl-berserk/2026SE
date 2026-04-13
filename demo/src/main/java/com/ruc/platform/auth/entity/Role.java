package com.ruc.platform.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色实体类
 * 对应数据库表：t_role
 */
@Data
@TableName("t_role")
public class Role {

    /**
     * 角色ID（主键）
     */
    @TableId
    private Long id;

    /**
     * 角色编码：student/counselor/admin
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
