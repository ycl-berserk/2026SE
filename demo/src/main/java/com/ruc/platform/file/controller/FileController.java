package com.ruc.platform.file.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ruc.platform.common.api.Result;
import com.ruc.platform.file.entity.FileMetadata;
import com.ruc.platform.file.service.FileService;
import com.ruc.platform.file.vo.FileUploadResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public Result<FileUploadResultVO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bizType", required = false, defaultValue = "attachment") String bizType) {
        long userId = StpUtil.getLoginIdAsLong();
        FileUploadResultVO result = fileService.uploadFile(file, bizType, userId);
        return Result.ok(result);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) throws IOException {
        FileMetadata metadata = fileService.getFileMetadata(id);

        byte[] fileBytes = Files.readAllBytes(Paths.get(metadata.getStoragePath()));

        String encodedFilename = URLEncoder.encode(metadata.getOriginName(), "UTF-8").replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                .contentType(MediaType.parseMediaType(metadata.getMimeType()))
                .contentLength(metadata.getFileSize())
                .body(fileBytes);
    }
}
