package com.ruc.platform.student.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生档案VO
 */
@Data
public class StudentProfileVO {

    /**
     * 档案ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别：1-男，2-女
     */
    private Integer gender;

    /**
     * 年级
     */
    private String grade;

    /**
     * 专业
     */
    private String major;

    /**
     * 班级
     */
    private String className;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 生源地
     */
    private String hometown;

    /**
     * 宿舍号
     */
    private String dormitory;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
