package com.ruc.platform.file.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件上传结果VO
 */
@Data
public class FileUploadResultVO {

    private Long id;

    private String originName;

    private Long fileSize;

    private LocalDateTime uploadTime;
}
