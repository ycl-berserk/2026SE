package com.ruc.platform.file.service;

import com.ruc.platform.file.vo.FileUploadResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface FileService {

    /**
     * 上传文件
     */
    FileUploadResultVO uploadFile(MultipartFile file, String bizType, Long uploaderId);

    /**
     * 获取文件元数据
     */
    com.ruc.platform.file.entity.FileMetadata getFileMetadata(Long fileId);
}
