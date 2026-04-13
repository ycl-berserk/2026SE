package com.ruc.platform.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 微信绑定实体类
 * 对应数据库表：t_wx_bind
 */
@Data
@TableName("t_wx_bind")
public class WxBind {

    /**
     * 绑定ID（主键）
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 微信OpenID
     */
    private String openId;

    /**
     * 微信UnionID
     */
    private String unionId;

    /**
     * 加密的SessionKey
     */
    private String sessionKeyEnc;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
