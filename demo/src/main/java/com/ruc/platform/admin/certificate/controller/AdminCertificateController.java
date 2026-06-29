package com.ruc.platform.admin.certificate.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ruc.platform.certificate.entity.ECertificate;
import com.ruc.platform.certificate.mapper.ECertificateMapper;
import com.ruc.platform.common.api.Result;
import com.ruc.platform.file.entity.FileMetadata;
import com.ruc.platform.file.service.FileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/certificates")
@RequiredArgsConstructor
public class AdminCertificateController {

    private final ECertificateMapper certificateMapper;
    private final FileService fileService;

    @GetMapping
    public Result<List<ECertificate>> listAll(@RequestParam(required = false) Integer status) {
        List<ECertificate> list;
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ECertificate> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ECertificate>();
        if (status != null) {
            wrapper.eq(ECertificate::getStatus, status);
        }
        wrapper.orderByDesc(ECertificate::getSubmitTime);
        list = certificateMapper.selectList(wrapper);
        return Result.ok(list);
    }

    @GetMapping("/{id}")
    public Result<ECertificate> getDetail(@PathVariable Long id) {
        ECertificate cert = certificateMapper.selectById(id);
        if (cert == null) {
            return Result.fail(404, "申请不存在");
        }
        return Result.ok(cert);
    }

    @PostMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id, @Valid @RequestBody ApproveDTO approveDTO) {
        Long adminId = StpUtil.getLoginIdAsLong();
        ECertificate cert = certificateMapper.selectById(id);
        if (cert == null) {
            return Result.fail(404, "申请不存在");
        }
        FileMetadata metadata = fileService.getFileMetadata(approveDTO.getCertificateFileId());
        if (!"certificate-pdf".equals(metadata.getBizType())) {
            return Result.fail(400, "请上传电子证明 PDF 文件");
        }
        if (!isPdf(metadata)) {
            return Result.fail(400, "证明附件必须是 PDF 文件");
        }
        cert.setStatus(2);
        cert.setApprovedBy(adminId);
        cert.setApprovedAt(LocalDateTime.now());
        cert.setRejectReason(null);
        cert.setCertificateFileId(approveDTO.getCertificateFileId());
        cert.setUpdatedAt(LocalDateTime.now());
        certificateMapper.updateById(cert);
        log.info("审批通过电子证明申请，id: {}, adminId: {}, fileId: {}", id, adminId, cert.getCertificateFileId());
        return Result.ok();
    }

    @PostMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id, @RequestBody(required = false) RejectDTO rejectDTO) {
        ECertificate cert = certificateMapper.selectById(id);
        if (cert == null) {
            return Result.fail(404, "申请不存在");
        }
        cert.setStatus(3);
        cert.setRejectReason(rejectDTO != null ? rejectDTO.getRejectReason() : null);
        cert.setUpdatedAt(LocalDateTime.now());
        certificateMapper.updateById(cert);
        log.info("驳回电子证明申请，id: {}, reason: {}", id, cert.getRejectReason());
        return Result.ok();
    }

    private boolean isPdf(FileMetadata metadata) {
        String mimeType = metadata.getMimeType();
        String originName = metadata.getOriginName();
        return "application/pdf".equalsIgnoreCase(mimeType)
                || (originName != null && originName.toLowerCase().endsWith(".pdf"));
    }

    @Data
    public static class ApproveDTO {
        @NotNull(message = "请上传证明 PDF")
        private Long certificateFileId;
    }

    @Data
    public static class RejectDTO {
        private String rejectReason;
    }
}
