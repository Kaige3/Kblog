package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.pojo.Tag;
import org.mapstruct.Mapper;


/**
 * 标签(SgTag)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-23 19:52:01
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
