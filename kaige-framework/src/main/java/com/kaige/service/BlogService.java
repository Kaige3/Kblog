package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.Blog;
import com.kaige.entity.PageResult;
import com.kaige.entity.Result;
import com.kaige.model.vo.BlogDetail;
import com.kaige.model.vo.BlogInfo;
import com.kaige.model.vo.NewBlog;
import com.kaige.model.vo.RandomBlog;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * (Blog)表服务接口
 *
 * @author makejava
 * @since 2024-09-06 13:53:59
 */
@Service
public interface BlogService extends IService<Blog> {

    PageResult<BlogInfo> getBlogInfoListByIsPublished(Integer pageNum);

    Result archives();

    PageResult<BlogInfo> getBloginfoListByCategoryNameAndIsPublished(String categoryName, Integer pageNum);

    List<NewBlog> getNewBlogListByIsPublished();

    List<RandomBlog> getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend();

    PageResult<BlogInfo> getBloginfoListByTagNameAndIsPublished(String tagName, Integer pageNum);

    Boolean getCommentEnabledByBlogId(Long blogId);

    Boolean getPublishedByBlogId(Long blogId);

    String getBlogPassword(Long blogId);

    BlogDetail getBlogByIdAndIsPublished(Long id) throws NotFoundException;

    void updateViewsToRedis(Long id);
}

