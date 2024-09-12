package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * (Tag)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-06 21:59:46
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> selectNotId();

    List<Tag> getTagsByBlogId(Long id);
}
