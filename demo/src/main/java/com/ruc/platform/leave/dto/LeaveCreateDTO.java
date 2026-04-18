package com.ruc.platform.leave.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 创建请假申请 DTO
 */
@Data
public class LeaveCreateDTO {

    @NotBlank(message = "请假标题不能为空")
    private String title;

    @NotBlank(message = "请假事由不能为空")
    private String reason;

    @NotNull(message = "请假开始日期不能为空")
    private LocalDate leaveStartDate;

    @NotNull(message = "请假结束日期不能为空")
    private LocalDate leaveEndDate;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    private Long fileId;
}
