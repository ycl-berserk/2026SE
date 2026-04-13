package com.ruc.platform.file.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件元数据实体
 */
@Data
@TableName("file_metadata")
public class FileMetadata {

    @TableId
    private Long id;

    private String bizType;

    private String originName;

    private String storedName;

    private String storagePath;

    private String mimeType;

    private Long fileSize;

    private Long uploaderId;

    private Integer status;

    private LocalDateTime createdAt;
}
