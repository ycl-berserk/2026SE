package com.ruc.platform.student.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentProfileVO {

    private Long id;

    private Long userId;

    private String studentNo;

    private String realName;

    private String phone;

    private String email;

    private Integer gender;

    private String grade;

    private String major;

    private String className;

    private String politicalStatus;

    private String avatarUrl;

    private String bio;

    private String hometown;

    private String dormitory;

    private String authType;

    private LocalDateTime updatedAt;
}
