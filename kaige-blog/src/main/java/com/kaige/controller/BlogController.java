package com.kaige.controller;

import com.kaige.constant.JwtConstants;
import com.kaige.entity.Blog;
import com.kaige.entity.PageResult;
import com.kaige.entity.Result;
import com.kaige.entity.User;
import com.kaige.model.vo.BlogDetail;
import com.kaige.model.vo.BlogInfo;
import com.kaige.service.UserService;
import com.kaige.service.impl.UserServiceImpl;
import com.kaige.utils.JwtUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kaige.service.BlogService;

import java.util.List;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserServiceImpl userService;

    /**
     * 这是一个测试
     */
//    @GetMapping("/list")
//    public Result test(){
//
//        blogService.list();
//        return Result.ok("执行成功");
//    }
    /**
     * 首页缩略图
     * 按置顶、创建时间排序 分页查询博客简要信息列表
     * 显示在首页
     */
    @GetMapping("/blogs")
    public Result blogs(@RequestParam(defaultValue = "1") Integer pageNum){
        PageResult<BlogInfo> pageResult = blogService.getBlogInfoListByIsPublished(pageNum);
        return Result.ok("请求成功",pageResult);
    }
    /**
     * 按id获取公开博客详情
     *
     * @param id  博客id
     * @param jwt 密码保护文章的访问Token
     * @return
     */
    @GetMapping("/blog")
    public Result getBlog(@RequestParam Long id,
                          @RequestHeader(value = "Authorization", defaultValue = "") String jwt) throws NotFoundException {
        BlogDetail blog = blogService.getBlogByIdAndIsPublished(id);
        //对密码保护的文章校验Token
        if (!"".equals(blog.getPassword())) {
            if (JwtUtils.judgeTokenIsExist(jwt)) {
                try {
                    String subject = JwtUtils.getTokenBody(jwt).getSubject();
                    if (subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
                        //博主身份Token
                        String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                        User admin = (User) userService.loadUserByUsername(username);
                        if (admin == null) {
                            return Result.create(403, "博主身份Token已失效，请重新登录！");
                        }
                    } else {
                        //经密码验证后的Token
                        Long tokenBlogId = Long.parseLong(subject);
                        //博客id不匹配，验证不通过，可能博客id改变或客户端传递了其它密码保护文章的Token
                        if (!tokenBlogId.equals(id)) {
                            return Result.create(403, "Token不匹配，请重新验证密码！");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.create(403, "Token已失效，请重新验证密码！");
                }
            } else {
                return Result.create(403, "此文章受密码保护，请验证密码！");
            }
            blog.setPassword("");
        }
        blogService.updateViewsToRedis(id);
        return Result.ok("获取成功", blog);
    }
}
