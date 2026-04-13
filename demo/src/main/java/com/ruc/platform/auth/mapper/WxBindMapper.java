package com.ruc.platform.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.auth.entity.WxBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 微信绑定Mapper接口
 */
@Mapper
public interface WxBindMapper extends BaseMapper<WxBind> {

    /**
     * 根据OpenID查询微信绑定记录
     * @param openId 微信OpenID
     * @return 微信绑定实体
     */
    @Select("SELECT * FROM t_wx_bind WHERE open_id = #{openId}")
    WxBind selectByOpenId(@Param("openId") String openId);
}
