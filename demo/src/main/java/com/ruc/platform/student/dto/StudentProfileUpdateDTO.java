package com.ruc.platform.student.dto;

import lombok.Data;

@Data
public class StudentProfileUpdateDTO {

    private String realName;

    private String phone;

    private String email;

    private Integer gender;

    private String grade;

    private String major;

    private String className;

    private String politicalStatus;

    private String authType;

    private String bio;

    private String hometown;

    private String dormitory;
}
