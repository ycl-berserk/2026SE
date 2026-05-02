package com.ruc.platform.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import com.ruc.platform.auth.dto.AccountLoginDTO;
import com.ruc.platform.auth.dto.AccountRegisterDTO;
import com.ruc.platform.auth.dto.WxLoginDTO;
import com.ruc.platform.auth.entity.Role;
import com.ruc.platform.auth.entity.User;
import com.ruc.platform.auth.entity.UserRole;
import com.ruc.platform.auth.mapper.RoleMapper;
import com.ruc.platform.auth.mapper.UserMapper;
import com.ruc.platform.auth.mapper.UserRoleMapper;
import com.ruc.platform.auth.vo.LoginVO;
import com.ruc.platform.auth.vo.UserVO;
import com.ruc.platform.common.api.ResultCode;
import com.ruc.platform.common.exception.BizException;
import com.ruc.platform.student.entity.StudentProfile;
import com.ruc.platform.student.mapper.StudentProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String ROLE_CODE_STUDENT = "student";
    private static final String AUTH_TYPE_STUDENT = "student";
    private static final String AUTH_TYPE_CADRE = "cadre";

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final StudentProfileMapper studentProfileMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO register(AccountRegisterDTO registerDTO) {
        String studentNo = clean(registerDTO.getStudentNo());
        if (userMapper.selectByStudentNo(studentNo) != null) {
            throw new BizException(ResultCode.BIZ_ERROR, "该学号已注册");
        }
        if (!clean(registerDTO.getPassword()).equals(clean(registerDTO.getConfirmPassword()))) {
            throw new BizException(ResultCode.PARAM_ERROR, "两次输入的密码不一致");
        }

        Role studentRole = roleMapper.selectByRoleCode(ROLE_CODE_STUDENT);
        if (studentRole == null) {
            throw new BizException(ResultCode.SYSTEM_ERROR, "学生角色不存在，请检查初始化数据");
        }

        User user = new User();
        user.setStudentNo(studentNo);
        user.setRealName(clean(registerDTO.getRealName()));
        user.setPasswordHash(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhone(clean(registerDTO.getPhone()));
        user.setEmail(clean(registerDTO.getEmail()));
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(studentRole.getId());
        userRoleMapper.insert(userRole);

        StudentProfile profile = new StudentProfile();
        profile.setUserId(user.getId());
        profile.setStudentNo(studentNo);
        profile.setGender(registerDTO.getGender());
        profile.setGrade(clean(registerDTO.getGrade()));
        profile.setMajor(clean(registerDTO.getMajor()));
        profile.setClassName(clean(registerDTO.getClassName()));
        profile.setPoliticalStatus(clean(registerDTO.getPoliticalStatus()));
        profile.setBio(clean(registerDTO.getBio()));
        profile.setHometown(clean(registerDTO.getHometown()));
        profile.setDormitory(clean(registerDTO.getDormitory()));
        profile.setAuthType(normalizeAuthType(registerDTO.getAuthType()));
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        studentProfileMapper.insert(profile);

        log.info("账号注册成功，userId: {}, studentNo: {}", user.getId(), studentNo);
        return buildLoginVO(user);
    }

    @Override
    public LoginVO login(AccountLoginDTO loginDTO) {
        User user = userMapper.selectByStudentNo(clean(loginDTO.getStudentNo()));
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new BizException(ResultCode.UNAUTHORIZED, "学号或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BizException(ResultCode.FORBIDDEN, "账号已被禁用");
        }
        return buildLoginVO(user);
    }

    @Override
    public LoginVO wxLogin(WxLoginDTO wxLoginDTO) {
        throw new BizException(ResultCode.FORBIDDEN, "当前版本不使用微信绑定登录，请改用学号密码登录");
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
    }

    private LoginVO buildLoginVO(User user) {
        StpUtil.login(user.getId());
        LoginVO loginVO = new LoginVO();
        loginVO.setNeedBind(false);
        loginVO.setToken(StpUtil.getTokenValue());
        loginVO.setUser(buildUserVO(user));
        return loginVO;
    }

    private UserVO buildUserVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setRealName(user.getRealName());
        userVO.setStudentNo(user.getStudentNo());
        List<String> roles = userMapper.selectRoleCodesByUserId(user.getId());
        userVO.setRoles(roles);

        StudentProfile profile = studentProfileMapper.selectByUserId(user.getId());
        if (profile != null) {
            userVO.setAuthType(profile.getAuthType());
            userVO.setClassName(profile.getClassName());
            userVO.setAvatarUrl(profile.getAvatarUrl());
        }
        return userVO;
    }

    private String normalizeAuthType(String authType) {
        String normalized = clean(authType);
        if (normalized.isEmpty()) {
            return AUTH_TYPE_STUDENT;
        }
        if (AUTH_TYPE_STUDENT.equalsIgnoreCase(normalized)) {
            return AUTH_TYPE_STUDENT;
        }
        if (AUTH_TYPE_CADRE.equalsIgnoreCase(normalized)) {
            return AUTH_TYPE_CADRE;
        }
        throw new BizException(ResultCode.PARAM_ERROR, "身份类型仅支持 student 或 cadre");
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
