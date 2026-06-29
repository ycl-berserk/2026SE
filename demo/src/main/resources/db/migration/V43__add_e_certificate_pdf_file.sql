-- Add approved PDF attachment for electronic certificate applications.

ALTER TABLE e_certificate
    ADD COLUMN IF NOT EXISTS certificate_file_id BIGINT;

COMMENT ON COLUMN e_certificate.certificate_file_id IS '审批通过后返回给学生下载的证明 PDF 文件ID';
