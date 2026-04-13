package com.ruc.platform.auth.vo;

import lombok.Data;

/**
 * 登录响应VO
 */
@Data
public class LoginVO {

    /**
     * 平台Token
     */
    private String token;

    /**
     * 是否需要绑定账号
     */
    private Boolean needBind;

    /**
     * 用户信息
     */
    private UserVO user;
}
