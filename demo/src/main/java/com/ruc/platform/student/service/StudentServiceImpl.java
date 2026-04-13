package com.ruc.platform.student.service;

import com.ruc.platform.auth.entity.User;
import com.ruc.platform.auth.mapper.UserMapper;
import com.ruc.platform.common.api.ResultCode;
import com.ruc.platform.common.exception.BizException;
import com.ruc.platform.student.dto.StudentProfileUpdateDTO;
import com.ruc.platform.student.entity.StudentProfile;
import com.ruc.platform.student.mapper.StudentProfileMapper;
import com.ruc.platform.student.vo.StudentProfileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentProfileMapper studentProfileMapper;
    private final UserMapper userMapper;

    @Override
    public StudentProfileVO getProfileByUserId(Long userId) {
        StudentProfile profile = studentProfileMapper.selectByUserId(userId);
        if (profile == null) {
            throw new BizException(ResultCode.NOT_FOUND, "学生档案不存在");
        }
        return convertToVO(profile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentProfileVO updateProfile(Long userId, StudentProfileUpdateDTO updateDTO) {
        StudentProfile profile = studentProfileMapper.selectByUserId(userId);
        if (profile == null) {
            throw new BizException(ResultCode.NOT_FOUND, "学生档案不存在");
        }

        if (updateDTO.getGender() != null) {
            profile.setGender(updateDTO.getGender());
        }
        if (updateDTO.getBio() != null) {
            profile.setBio(updateDTO.getBio());
        }
        if (updateDTO.getHometown() != null) {
            profile.setHometown(updateDTO.getHometown());
        }
        if (updateDTO.getDormitory() != null) {
            profile.setDormitory(updateDTO.getDormitory());
        }

        profile.setUpdatedAt(LocalDateTime.now());
        studentProfileMapper.updateById(profile);
        log.info("更新学生档案成功，userId: {}", userId);
        return convertToVO(profile);
    }

    private StudentProfileVO convertToVO(StudentProfile profile) {
        StudentProfileVO vo = new StudentProfileVO();
        BeanUtils.copyProperties(profile, vo);
        User user = userMapper.selectById(profile.getUserId());
        if (user != null) {
            vo.setRealName(user.getRealName());
        }
        return vo;
    }
}
