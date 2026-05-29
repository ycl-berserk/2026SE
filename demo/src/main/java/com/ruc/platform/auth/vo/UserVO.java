package com.ruc.platform.auth.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {

    private Long id;

    private String realName;

    private String studentNo;

    private String accountType;

    private String phone;

    private String email;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<String> roles;

    private String authType;

    private String className;

    private String avatarUrl;
}
