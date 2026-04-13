package com.ruc.platform.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import com.ruc.platform.auth.dto.WxLoginDTO;
import com.ruc.platform.auth.entity.User;
import com.ruc.platform.auth.entity.WxBind;
import com.ruc.platform.auth.mapper.UserMapper;
import com.ruc.platform.auth.mapper.WxBindMapper;
import com.ruc.platform.auth.vo.LoginVO;
import com.ruc.platform.auth.vo.UserVO;
import com.ruc.platform.common.api.ResultCode;
import com.ruc.platform.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final WxBindMapper wxBindMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO wxLogin(WxLoginDTO wxLoginDTO) {
        String openId = "mock_openid_" + wxLoginDTO.getCode();
        log.info("微信登录，openid: {}", openId);

        WxBind wxBind = wxBindMapper.selectByOpenId(openId);
        LoginVO loginVO = new LoginVO();
        if (wxBind == null) {
            loginVO.setNeedBind(true);
            return loginVO;
        }

        User user = userMapper.selectById(wxBind.getUserId());
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "用户不存在");
        }
        if (user.getStatus() != 1) {
            throw new BizException(ResultCode.FORBIDDEN, "账号已被禁用");
        }

        wxBind.setLastLoginAt(LocalDateTime.now());
        wxBindMapper.updateById(wxBind);

        StpUtil.login(user.getId());
        loginVO.setNeedBind(false);
        loginVO.setToken(StpUtil.getTokenValue());
        loginVO.setUser(buildUserVO(user));
        return loginVO;
    }

    @Override
    public UserVO getCurrentUser() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "用户不存在");
        }
        return buildUserVO(user);
    }

    @Override
    public void logout() {
        StpUtil.logout();
        log.info("用户退出登录");
    }

    private UserVO buildUserVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setRealName(user.getRealName());
        userVO.setStudentNo(user.getStudentNo());
        List<String> roles = userMapper.selectRoleCodesByUserId(user.getId());
        userVO.setRoles(roles);
        return userVO;
    }
}
