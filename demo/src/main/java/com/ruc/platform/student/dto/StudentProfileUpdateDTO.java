package com.ruc.platform.student.dto;

import lombok.Data;

/**
 * 学生档案更新DTO
 */
@Data
public class StudentProfileUpdateDTO {

    /**
     * 性别：1-男，2-女
     */
    private Integer gender;

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
}
