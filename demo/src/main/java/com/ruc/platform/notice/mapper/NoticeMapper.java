package com.ruc.platform.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruc.platform.notice.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知Mapper
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
