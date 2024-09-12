package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.pojo.ArticleTag;
import org.mapstruct.Mapper;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-25 18:48:22
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
