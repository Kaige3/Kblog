package com.kaige.controller;

import com.kaige.entity.Blog;
import com.kaige.entity.Category;
import com.kaige.entity.Result;
import com.kaige.entity.Tag;
import com.kaige.model.vo.NewBlog;
import com.kaige.model.vo.RandomBlog;
import com.kaige.service.BlogService;
import com.kaige.service.CategoryService;
import com.kaige.service.SiteSettingService;
import com.kaige.service.TagService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点配置信息，最新博客推荐，分类列表，标签云，随机博客
 */
@RestController
public class IndexController {

    @Autowired
    private SiteSettingService siteSettingService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @GetMapping("/site")
    public Result site(){
        Map<String,Object> map = siteSettingService.getSiteInfo();
        List<NewBlog> newBlogs = blogService.getNewBlogListByIsPublished();
        List<Category> categories = categoryService.getCategoryNameList();
        List<Tag> tags = tagService.getTagListNotId();
        List<RandomBlog> randomBlogs = blogService.getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend();
        map.put("newBlogList",newBlogs);
        map.put("categoryList",categories);
        map.put("tagList",tags);
        map.put("randomBlogList",randomBlogs);
        return Result.ok("请求成功",map);
    }

}
