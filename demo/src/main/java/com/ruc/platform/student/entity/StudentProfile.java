package com.ruc.platform.student.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("student_profile")
public class StudentProfile {

    @TableId
    private Long id;

    private Long userId;

    private String studentNo;

    private Integer gender;

    private String grade;

    private String major;

    private String className;

    private String politicalStatus;

    private String idCard;

    private String avatarUrl;

    private String bio;

    private String hometown;

    private String dormitory;

    private String authType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
