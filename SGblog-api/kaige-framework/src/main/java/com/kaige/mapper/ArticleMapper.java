package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.pojo.Article;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Article> selectAllArticles();


}
