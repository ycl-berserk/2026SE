package com.ruc.platform.home.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ruc.platform.common.api.Result;
import com.ruc.platform.home.service.HomeService;
import com.ruc.platform.home.vo.HomeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    /**
     * 获取首页聚合数据
     */
    @GetMapping
    public Result<HomeVO> getHomeData() {
        long userId = StpUtil.getLoginIdAsLong();
        log.info("获取首页数据，userId: {}", userId);
        return Result.ok(homeService.getHomeData(userId));
    }
}
