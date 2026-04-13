package com.ruc.platform.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户角色关联实体类
 * 对应数据库表：t_user_role
 */
@Data
@TableName("t_user_role")
public class UserRole {

    /**
     * 关联ID（主键）
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
