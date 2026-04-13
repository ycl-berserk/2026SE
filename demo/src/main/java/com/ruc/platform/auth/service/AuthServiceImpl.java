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
import org.springframework.core.env.Environment;
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
    private final Environment environment;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO wxLogin(WxLoginDTO wxLoginDTO) {
        String code = wxLoginDTO.getCode();
        log.info("微信登录，code: {}", code);

        // 开发环境：使用 demo code 自动登录为测试用户 1001
        if ("demo".equals(code) && isDevEnvironment()) {
            User user = userMapper.selectById(1001L);
            if (user == null) {
                throw new BizException(ResultCode.NOT_FOUND, "测试用户不存在，请先运行数据库初始化");
            }
            log.info("开发环境自动登录为测试用户: {} ({})", user.getRealName(), user.getStudentNo());
            return buildLoginVO(user);
        }

        String openId = "mock_openid_" + code;
        WxBind wxBind = wxBindMapper.selectByOpenId(openId);
        if (wxBind == null) {
            LoginVO loginVO = new LoginVO();
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

        return buildLoginVO(user);
    }

    /**
     * 判断是否为开发环境
     */
    private boolean isDevEnvironment() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            return true; // 默认开发环境
        }
        for (String profile : activeProfiles) {
            String p = profile.toLowerCase();
            if (p.equals("h2") || p.equals("dev") || p.equals("test")
                || p.equals("postgres") || p.equals("kingbase")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 构建登录返回对象
     */
    private LoginVO buildLoginVO(User user) {
        StpUtil.login(user.getId());
        LoginVO loginVO = new LoginVO();
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
