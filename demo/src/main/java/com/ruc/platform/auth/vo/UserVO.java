package com.ruc.platform.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * 用户信息VO
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 头像URL
     */
    private String avatarUrl;
}
