package com.ruc.platform.file.service;

import com.ruc.platform.common.exception.BizException;
import com.ruc.platform.common.api.ResultCode;
import com.ruc.platform.file.entity.FileMetadata;
import com.ruc.platform.file.mapper.FileMapper;
import com.ruc.platform.file.vo.FileUploadResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadResultVO uploadFile(MultipartFile file, String bizType, Long uploaderId) {
        if (file.isEmpty()) {
            throw new BizException(ResultCode.PARAM_ERROR, "文件不能为空");
        }

        try {
            // 生成存储文件名
            String storedName = UUID.randomUUID().toString().replace("-", "");
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String storedNameWithExt = storedName + extension;

            // 创建目录
            String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path uploadDir = Paths.get(uploadPath, dateStr);
            Files.createDirectories(uploadDir);

            // 保存文件
            Path filePath = uploadDir.resolve(storedNameWithExt);
            Files.write(filePath, file.getBytes());

            String storagePath = uploadPath + "/" + dateStr + "/" + storedNameWithExt;

            // 保存元数据
            FileMetadata metadata = new FileMetadata();
            metadata.setOriginName(originalFilename);
            metadata.setStoredName(storedNameWithExt);
            metadata.setStoragePath(storagePath);
            metadata.setMimeType(file.getContentType());
            metadata.setFileSize(file.getSize());
            metadata.setBizType(bizType);
            metadata.setUploaderId(uploaderId);
            metadata.setStatus(1);

            fileMapper.insert(metadata);

            log.info("文件上传成功，fileId: {}, fileName: {}", metadata.getId(), originalFilename);

            // 构建返回结果
            FileUploadResultVO result = new FileUploadResultVO();
            result.setId(metadata.getId());
            result.setOriginName(originalFilename);
            result.setFileSize(file.getSize());
            result.setUploadTime(LocalDateTime.now());

            return result;

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BizException(ResultCode.BIZ_ERROR, "文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public FileMetadata getFileMetadata(Long fileId) {
        FileMetadata metadata = fileMapper.selectById(fileId);
        if (metadata == null) {
            throw new BizException(ResultCode.NOT_FOUND, "文件不存在");
        }
        return metadata;
    }
}
