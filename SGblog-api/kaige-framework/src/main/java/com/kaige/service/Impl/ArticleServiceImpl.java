package com.kaige.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.constants.SystemConstants;
import com.kaige.entity.dto.AddArticleDto;
import com.kaige.entity.pojo.Article;
import com.kaige.entity.pojo.ArticleTag;
import com.kaige.entity.pojo.Category;
import com.kaige.entity.vo.*;
import com.kaige.result.ResponseResult;
import com.kaige.mapper.ArticleMapper;
import com.kaige.service.ArticleService;
import com.kaige.service.ArticleTagService;
import com.kaige.service.CategoryService;
import com.kaige.utils.BeanCopyUtils;
import com.kaige.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;
    @Override
    public ResponseResult hotArticleList() {
//        查询包装
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        状态为已发布
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//        按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
//        只查询10条
        Page<Article> page = new Page(1, 10);
        page(page, queryWrapper);
        List<Article> articles = page.getRecords();
        // 从Redis中获取每篇文章的viewCount，并更新到文章对象中
        for (Article article : articles) {
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            if (viewCount != null) {
                article.setViewCount(viewCount.longValue());
            }
        }

//      Bean拷贝
        List<HotArticleVo> arrayLists = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(arrayLists);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {

//        包装查询条件对象
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        如果categoryId 有 就要查询时传入相同的
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
//        状态是正式发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//        对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);
//        分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
//        查询categoryName
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
//        封装查询结果
        for (Article article : articles) {
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            if (viewCount != null) {
                article.setViewCount(viewCount.longValue());
            }
        }
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
//        根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
//        转换为VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
//        根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto article) {
//        添加博客
        Article article1 = BeanCopyUtils.copyBean(article, Article.class);
        save(article1);
        List<ArticleTag> articleTags = article.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
//        添加博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public PageVo selectArtclePage(Integer pageNum, Integer pageSize, Article article) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle,article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()),Article::getSummary,article.getSummary());

        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(articles);
        return pageVo;
    }

    @Override
    public AdminArticleDetailVo selectById(Long id) {

        //        根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
//        转换为VO
        AdminArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, AdminArticleDetailVo.class);
//        根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Tag::getId,id);
        queryWrapper.eq(ArticleTag::getArticleId,article.getId());
//        List<Tag> list = tagService.list(queryWrapper);
        List<ArticleTag> list = articleTagService.list(queryWrapper);
//        articleDetailVo.setTags(list);
        List<Long> tagIds = list.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        articleDetailVo.setTags(tagIds);

        return articleDetailVo;
    }

    @Override
    public ResponseResult archives() {

        List<Article> articles = list();
        // 转换为 "年-月" 格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");

        Map<String, List<Map<String, Object>>> blogMap = articles.stream()
                .collect(Collectors.groupingBy(
                        article -> article.getCreateTime().format(formatter),
                        LinkedHashMap::new, // 保持排序
                        Collectors.mapping(
                                article -> {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", article.getId());
                                    map.put("day", article.getCreateTime().getDayOfMonth() + "日");
                                    map.put("title", article.getTitle());
                                    return map;
                                },
                                Collectors.toList()
                        )
                ));
//        计算文章总数
        int size = articles.size();

        Map<String, Object> result = new HashMap<>();
        result.put("blogMap", blogMap);
        result.put("count", size);
        // 创建 VO 对象
        BlogDataVO blogDataVO = new BlogDataVO(blogMap, size);
        return  ResponseResult.okResult(blogDataVO);
    }

}
