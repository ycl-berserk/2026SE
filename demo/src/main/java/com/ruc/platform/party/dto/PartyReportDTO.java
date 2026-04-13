package com.ruc.platform.party.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 思想汇报提交DTO
 */
@Data
public class PartyReportDTO {

    /**
     * 汇报标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 汇报内容
     */
    private String content;

    /**
     * 附件文件ID
     */
    private Long fileId;
}
