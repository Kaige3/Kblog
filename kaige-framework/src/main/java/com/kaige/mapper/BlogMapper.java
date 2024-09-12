package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaige.entity.Blog;
import com.kaige.model.vo.BlogDetail;
import com.kaige.model.vo.BlogInfo;
import com.kaige.model.vo.RandomBlog;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * (Blog)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-06 13:53:57
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    List<RandomBlog> getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend(Integer  limitNum );

    List<BlogInfo> getBlogInfoListByCategoryNameAndIsPublished(Page<Object> page, @RequestParam(value = "categoryName")String categoryName);

    BlogDetail getBlogByIdAndIsPublished(Long id);
}
