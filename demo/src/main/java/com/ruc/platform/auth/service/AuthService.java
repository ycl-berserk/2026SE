package com.ruc.platform.auth.service;

import com.ruc.platform.auth.dto.AccountLoginDTO;
import com.ruc.platform.auth.dto.AccountRegisterDTO;
import com.ruc.platform.auth.dto.WxLoginDTO;
import com.ruc.platform.auth.vo.LoginVO;
import com.ruc.platform.auth.vo.UserVO;

public interface AuthService {

    LoginVO register(AccountRegisterDTO registerDTO);

    LoginVO login(AccountLoginDTO loginDTO);

    LoginVO wxLogin(WxLoginDTO wxLoginDTO);

    UserVO getCurrentUser();

    void logout();
}
