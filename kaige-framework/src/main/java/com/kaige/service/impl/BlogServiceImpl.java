package com.kaige.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.*;
import com.kaige.mapper.BlogMapper;
import com.kaige.model.vo.*;
import com.kaige.service.*;
import com.kaige.utils.BeanCopyUtils;
import com.kaige.utils.JacksonUtils;
import com.kaige.utils.markdown.MarkdownUtils;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.poi.hpsf.NoFormatIDException;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (Blog)表服务实现类
 *
 * @author makejava
 * @since 2024-09-06 13:53:59
 */
@Service
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CommentService commentService;


    //随机博客显示5条
    private static final int randomBlogLimitNum = 5;
    //最新推荐博客显示3条
    private static final int newBlogPageSize = 3;
    //每页显示5条博客简介
    private static final int pageSize = 5;
    //博客简介列表排序方式
//    private static final String orderBy = "is_top desc, create_time desc";
    //私密博客提示
    private static final String PRIVATE_BLOG_DESCRIPTION = "此文章受密码保护！";
    @Override
    public PageResult<BlogInfo> getBlogInfoListByIsPublished(Integer pageNum) {
        String redisKey = RedisKeyConstants.HOME_BLOG_INFO_LIST;
//        A.redis有当前页缓存
        PageResult<BlogInfo> pageResult = redisService.getBlogInfoPageResultByHash(redisKey,pageNum);
        if(pageResult != null){
//            将浏览量设置为最新的
            setBlogViewsFromRedisToPageResult(pageResult);
            return pageResult;
        }

//        B.redis没有，先从数据库查询，然后添加到redis
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getIsPublished,true);
        queryWrapper.orderByDesc(Blog::getIsTop,Blog::getCreateTime);

        Page<Blog> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
//        PageResult<Blog> objectPageResult = new PageResult<>(page.getTotal(),page.getRecords());
//        Page<Blog> page = new Page<>(pageNum,pageSize);
//        page(page,queryWrapper);
        List<Blog> records = page.getRecords();

        // 转换
        // 3. 查询每篇博客的分类和标签，并填充到 BlogInfo 中
        List<BlogInfo> blogInfos = records.stream().map(blog -> {
            BlogInfo blogInfo = BeanCopyUtils.copyBean(blog, BlogInfo.class);

            blogInfo.setTop(blog.getIsTop());

            // 查询分类
            Category category = categoryService.getById(blog.getCategoryId());
            blogInfo.setCategory(category);

            // 查询标签

            blogInfo.setTags(tagService.getTagsByBlogId(blog.getId()));


//          填充Privacy信息
            if (!"".equals(blogInfo.getPassword())){
                blogInfo.setPrivacy(true);
                blogInfo.setPassword("");
                blogInfo.setDescription(PRIVATE_BLOG_DESCRIPTION);
            }else {
                blogInfo.setPrivacy(false);
                blogInfo.setDescription(MarkdownUtils.markdownToHtml(blogInfo.getDescription()));
            }

            return blogInfo;
        }).collect(Collectors.toList());

//        List<BlogInfo> blogInfos = BeanCopyUtils.copyBeanList(records, BlogInfo.class);
//       封装分页结果
        PageResult<BlogInfo> result = new PageResult<>(page.getTotal(), blogInfos);
//        设置浏览量
        setBlogViewsFromRedisToPageResult(result);
        //添加首页缓存
        redisService.saveKVToHash(redisKey, pageNum, result);
        return result;
    }

    @Override
    public Result archives() {
        List<Blog> list = list();
//        转换为年-月 格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");

        Map<String, List<Map<String, Object>>> collect = list.stream()
                .collect(Collectors.groupingBy(
                        Blog -> {
                            // Convert Date to LocalDateTime
                            LocalDateTime localDateTime = Blog.getCreateTime().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime();
                            // Format the date
                            return localDateTime.format(formatter);
                        },
                        LinkedHashMap::new,
                        Collectors.mapping(
                                Blog -> {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("id", Blog.getId());
                                    LocalDateTime localDateTime = Blog.getCreateTime().toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime();
                                    map.put("day", localDateTime.getDayOfMonth() + "日");
                                    map.put("title", Blog.getTitle());
                                    return map;
                                },
                                Collectors.toList()
                        )
                ));
//        计算文章总数
        int size = list.size();
        Map<String, Object> map = new HashMap<>();
        map.put("blogMap",collect);
        map.put("count",size);

        BlogDataVO archivesVo = new BlogDataVO(collect,size);
        return Result.ok("执行成功",archivesVo);
    }

    @Override
    public PageResult<BlogInfo> getBloginfoListByCategoryNameAndIsPublished(@RequestParam String categoryName, Integer pageNum) {
//        这样的查询只是针对category 和 blog的单表查询，实际上需要用到联表，
//        但是只要category 表中 没有 blogs这个字段的话，这个查询是有效的
        LambdaQueryWrapper<Category> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Category::getName,categoryName);
        Category one = categoryService.getOne(queryWrapper1);
        Long id = one.getId();

        if (id == null){
            log.info("id为空");
        }

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getCategoryId,id);
        queryWrapper.eq(Blog::getIsPublished,true);

        Page<Blog> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
//        转换

        List<Blog> records = page.getRecords();


        // 3. 查询每篇博客的分类和标签，并填充到 BlogInfo 中
        List<BlogInfo> blogInfos = records.stream().map(blog -> {

            BlogInfo blogInfo = BeanCopyUtils.copyBean(blog, BlogInfo.class);

            blogInfo.setTop(blog.getIsTop());
            // 查询分类
            Category category = categoryService.getById(blog.getCategoryId());
            blogInfo.setCategory(category);

            // 查询标签
            blogInfo.setTags(tagService.getTagsByBlogId(blog.getId()));

//            填充Privacy信息
            if (!"".equals(blogInfo.getPassword())){
                blogInfo.setPrivacy(true);
                blogInfo.setPassword("");
                blogInfo.setDescription(PRIVATE_BLOG_DESCRIPTION);
            }else {
                blogInfo.setPrivacy(false);
                blogInfo.setDescription(MarkdownUtils.markdownToHtml(blogInfo.getDescription()));
            }

            return blogInfo;
        }).collect(Collectors.toList());

//        List<BlogInfo> blogInfos = BeanCopyUtils.copyBeanList(records, BlogInfo.class);
//       封装分页结果
        PageResult<BlogInfo> result = new PageResult(page.getTotal(), blogInfos);

        return result;
       /* Page<Object> page = new Page<>(pageNum, pageSize);
        List<BlogInfo> blogInfos = blogMapper.getBlogInfoListByCategoryNameAndIsPublished(page,categoryName);
        blogInfos.stream().map(blogInfo->{
            List<Tag> tags = tagService.getTagsByBlogId(blogInfo.getId());
            blogInfo.setTags(tags);

            return blogInfo;
        }).collect(Collectors.toList());

        PageResult<BlogInfo> pageResult = new PageResult<>(page.getPages(), blogInfos);
        return pageResult;*/
    }

    @Override
    public List<NewBlog> getNewBlogListByIsPublished() {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getIsPublished,true);
        queryWrapper.orderByDesc(Blog::getCreateTime);
        List<Blog> list = list(queryWrapper);
        List<NewBlog> newBlogs = BeanCopyUtils.copyBeanList(list, NewBlog.class);
        return newBlogs;
    }

    @Override
    public List<RandomBlog> getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend() {
        return blogMapper.getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend(randomBlogLimitNum);
    }

    @Override
    public PageResult<BlogInfo> getBloginfoListByTagNameAndIsPublished(String tagName, Integer pageNum) {
        LambdaQueryWrapper<Tag> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Tag::getName,tagName);
        Tag one = tagService.getOne(queryWrapper1);
        Long id = one.getId();

        if (id == null){
            log.info("id为空");
        }

        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getCategoryId,id);
        queryWrapper.eq(Blog::getIsPublished,true);

        Page<Blog> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Blog> records = page.getRecords();


        // 3. 查询每篇博客的分类和标签，并填充到 BlogInfo 中
        List<BlogInfo> blogInfos = records.stream().map(blog -> {

            BlogInfo blogInfo = BeanCopyUtils.copyBean(blog, BlogInfo.class);

            blogInfo.setTop(blog.getIsTop());
            // 查询分类
            Category category = categoryService.getById(blog.getCategoryId());
            blogInfo.setCategory(category);

            // 查询标签

            blogInfo.setTags(tagService.getTagsByBlogId(blog.getId()));

//            填充Privacy信息
            if (!"".equals(blogInfo.getPassword())){
                blogInfo.setPrivacy(true);
                blogInfo.setPassword("");
                blogInfo.setDescription(PRIVATE_BLOG_DESCRIPTION);
            }else {
                blogInfo.setPrivacy(false);
                blogInfo.setDescription(MarkdownUtils.markdownToHtml(blogInfo.getDescription()));
            }

            return blogInfo;
        }).collect(Collectors.toList());

//       封装分页结果
        PageResult<BlogInfo> result = new PageResult(page.getTotal(), blogInfos);
        return result;
    }

    @Override
    public Boolean getCommentEnabledByBlogId(Long blogId) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getIsCommentEnabled,true);
        queryWrapper.eq(Blog::getId,blogId);
        Blog one = getOne(queryWrapper);
        return one != null;
    }

    @Override
    public Boolean getPublishedByBlogId(Long blogId) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getIsPublished,true);
        queryWrapper.eq(Blog::getId,blogId);
        Blog one = getOne(queryWrapper);
        return one != null;
    }

    @Override
    public String getBlogPassword(Long blogId) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getId,blogId);
        Blog one = getOne(queryWrapper);
        return one.getPassword();
    }

    @Override
    public BlogDetail getBlogByIdAndIsPublished(Long id) throws NotFoundException {
        Blog byId = getById(id);
        BlogDetail blogDetail = BeanCopyUtils.copyBean(byId, BlogDetail.class);
        blogDetail.setAppreciation(byId.getIsAppreciation());
        blogDetail.setCommentEnabled(byId.getIsCommentEnabled());
        blogDetail.setTop(byId.getIsTop());

        blogDetail.setCategory(categoryService.getById(id));
        blogDetail.setTags(tagService.getTagsByBlogId(id));
//        BlogDetail blogDetail =  blogMapper.getBlogByIdAndIsPublished(id);
        if (blogDetail == null){
            throw new NotFoundException("没有这篇blog");
        }
        blogDetail.setContent(MarkdownUtils.markdownToHtmlExtensions(blogDetail.getContent()));
        int view = (int) redisService.getValueByHashKey(RedisKeyConstants.BLOG_VIEWS_MAP, blogDetail.getId());
        blogDetail.setViews(view);
        return blogDetail;
    }

    @Override
    public void updateViewsToRedis(Long blogId) {
        redisService.incrementByHashKey(RedisKeyConstants.BLOG_VIEWS_MAP, blogId, 1);
    }


    /**
     * 将浏览量设置为最新的
     */
    private void setBlogViewsFromRedisToPageResult(PageResult<BlogInfo> pageResult){
        String redisKey = RedisKeyConstants.BLOG_VIEWS_MAP;
        List<BlogInfo> blogInfos = pageResult.getList();
        for (int i = 0; i < blogInfos.size(); i++) {
            BlogInfo blogInfo = JacksonUtils.convertValue(blogInfos.get(i), BlogInfo.class);
            Long id = blogInfo.getId();
            int view = (int) redisService.getValueByHashKey(redisKey,id);
            blogInfo.setViews(view);
            blogInfos.set(i,blogInfo);
        }
    }
//    获取文章分类名
}
