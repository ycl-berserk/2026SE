package com.ruc.platform.auth.service;

import com.ruc.platform.auth.dto.WxLoginDTO;
import com.ruc.platform.auth.vo.LoginVO;
import com.ruc.platform.auth.vo.UserVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 微信登录
     * @param wxLoginDTO 微信登录参数
     * @return 登录结果
     */
    LoginVO wxLogin(WxLoginDTO wxLoginDTO);

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    UserVO getCurrentUser();

    /**
     * 退出登录
     */
    void logout();
}
