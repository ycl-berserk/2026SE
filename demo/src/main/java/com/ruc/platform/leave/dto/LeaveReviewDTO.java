package com.ruc.platform.leave.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 请假审批 DTO
 */
@Data
public class LeaveReviewDTO {

    @NotBlank(message = "审批意见不能为空")
    private String comment;
}
