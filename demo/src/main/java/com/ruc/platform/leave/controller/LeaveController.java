package com.ruc.platform.leave.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ruc.platform.common.api.Result;
import com.ruc.platform.leave.dto.LeaveCreateDTO;
import com.ruc.platform.leave.dto.LeaveReviewDTO;
import com.ruc.platform.leave.service.LeaveService;
import com.ruc.platform.leave.vo.LeaveApplicationDetailVO;
import com.ruc.platform.leave.vo.LeaveApplicationListItemVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 请假控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/me/applications")
    public Result<Map<String, Long>> create(@Valid @RequestBody LeaveCreateDTO createDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        Long id = leaveService.create(userId, createDTO);
        log.info("创建请假申请，userId: {}, id: {}", userId, id);
        return Result.ok(Map.of("id", id));
    }

    @GetMapping("/me/applications")
    public Result<List<LeaveApplicationListItemVO>> listMine() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(leaveService.listMine(userId));
    }

    @GetMapping("/me/applications/{id}")
    public Result<LeaveApplicationDetailVO> getMineDetail(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(leaveService.getMineDetail(userId, id));
    }

    @PostMapping("/reviewer/applications/{id}/approve")
    public Result<Void> approve(@PathVariable Long id, @Valid @RequestBody LeaveReviewDTO reviewDTO) {
        Long reviewerId = StpUtil.getLoginIdAsLong();
        leaveService.approve(reviewerId, id, reviewDTO.getComment());
        return Result.ok();
    }

    @PostMapping("/reviewer/applications/{id}/reject")
    public Result<Void> reject(@PathVariable Long id, @Valid @RequestBody LeaveReviewDTO reviewDTO) {
        Long reviewerId = StpUtil.getLoginIdAsLong();
        leaveService.reject(reviewerId, id, reviewDTO.getComment());
        return Result.ok();
    }
}
