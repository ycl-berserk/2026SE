package com.ruc.platform.student.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生档案实体类
 * 对应数据库表：student_profile
 */
@Data
@TableName("student_profile")
public class StudentProfile {

    /**
     * 档案ID（主键）
     */
    @TableId
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
     * 性别：1-男，2-女
     */
    private Integer gender;

    /**
     * 年级：如 2023
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
     * 政治面貌：群众/共青团员/预备党员/正式党员
     */
    private String politicalStatus;

    /**
     * 身份证号
     */
    private String idCard;

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
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
