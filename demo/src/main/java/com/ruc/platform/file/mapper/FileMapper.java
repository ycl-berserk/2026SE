package com.ruc.platform.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.file.entity.FileMetadata;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件Mapper
 */
@Mapper
public interface FileMapper extends BaseMapper<FileMetadata> {
}
