package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.BlogTag;
import org.apache.ibatis.annotations.Mapper;


/**
 * (BlogTag)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-09 23:32:12
 */
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

}
