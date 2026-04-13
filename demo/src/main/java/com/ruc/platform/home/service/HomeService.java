package com.ruc.platform.home.service;

import com.ruc.platform.home.vo.HomeVO;

/**
 * 首页服务接口
 */
public interface HomeService {

    /**
     * 获取首页聚合数据
     */
    HomeVO getHomeData(Long userId);
}
