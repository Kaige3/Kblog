package com.kaige.utils.comment;

import com.kaige.Enum.CommentOpenStateEnum;
import com.kaige.constant.PageConstants;
import com.kaige.entity.Comment;
import com.kaige.entity.User;
import com.kaige.model.vo.FriendInfo;
import com.kaige.service.AboutService;
import com.kaige.service.BlogService;
import com.kaige.service.FriendService;
import com.kaige.utils.HashUtils;
import com.kaige.utils.IpAddressUtils;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * 评论工具类
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
@Component
@DependsOn("springContextUtils")
public class CommentUtils {
    @Autowired
    private BlogService blogService;
    @Autowired
    private AboutService aboutService;
    @Autowired
    private FriendService friendService;

    /**
     * 新评论是否默认公开
     */
    private Boolean commentDefaultOpen;
    @Value("true")
    public void setCommentDefaultOpen(Boolean commentDefaultOpen) {
        this.commentDefaultOpen = commentDefaultOpen;
    }

    /**
     * 查询对应页面评论是否开启
     *
     * @param page   页面分类（0普通文章，1关于我，2友链）
     * @param blogId 如果page==0，需要博客id参数，校验文章是否公开状态
     * @return CommentOpenStateEnum
     */
    public CommentOpenStateEnum judgeCommentState(Integer page, Long blogId) {
        switch (page) {
            case PageConstants.BLOG:
                //普通博客
                Boolean commentEnabled = blogService.getCommentEnabledByBlogId(blogId);
                Boolean published = blogService.getPublishedByBlogId(blogId);
                if (commentEnabled == null || published == null) {
                    //未查询到此博客
                    return CommentOpenStateEnum.NOT_FOUND;
                } else if (!published) {
                    //博客未公开
                    return CommentOpenStateEnum.NOT_FOUND;
                } else if (!commentEnabled) {
                    //博客评论已关闭
                    return CommentOpenStateEnum.CLOSE;
                }
                //判断文章是否存在密码
                String password = blogService.getBlogPassword(blogId);
                if (!StringUtils.isEmpty(password)) {
                    return CommentOpenStateEnum.PASSWORD;
                }
                break;
            case PageConstants.ABOUT:
                //关于我页面
                if (!aboutService.getAboutCommentEnabled()) {
                    //页面评论已关闭
                    return CommentOpenStateEnum.CLOSE;
                }
                break;
            case PageConstants.FRIEND:
                //友链页面
                FriendInfo friendInfo = friendService.getFriendInfo(true, false);
                if (!friendInfo.getCommentEnabled()) {
                    //页面评论已关闭
                    return CommentOpenStateEnum.CLOSE;
                }
                break;
            default:
                break;
        }
        return CommentOpenStateEnum.OPEN;
    }

    public void setAdminComment(Comment comment, HttpServletRequest request, User admin) {
        setGeneralAdminComment(comment, admin);
        comment.setIp(IpAddressUtils.getIpAddress(request));
    }

    /**
     * 通用博主评论属性
     *
     * @param comment 评论DTO
     * @param admin   博主信息
     */
    private void setGeneralAdminComment(Comment comment, User admin) {
        comment.setAdminComment(true);
        comment.setCreateTime(new Date());
        comment.setPublished(true);
        comment.setAvatar(admin.getAvatar());
        comment.setWebsite("/");
        comment.setNickname(admin.getNickname());
        comment.setEmail(admin.getEmail());
        comment.setNotice(false);
    }

    /**
     * 设置访客评论属性
     *
     * @param comment 当前收到的评论
     * @param request 用于获取ip
     */
    public void setVisitorComment(Comment comment, HttpServletRequest request) {
        try {
            // 直接处理评论昵称
            comment.setNickname(comment.getNickname().trim());
            // 设置随机头像
            setCommentRandomAvatar(comment);
        } catch (Exception e) {
            e.printStackTrace();
            // 在出现异常时，也处理评论昵称和设置随机头像
            comment.setNickname(comment.getNickname().trim());
            setCommentRandomAvatar(comment);
        }
        //check website
        if (!isValidUrl(comment.getWebsite())) {
            comment.setWebsite("");
        }
        comment.setAdminComment(false);
        comment.setCreateTime(new Date());
        comment.setPublished(commentDefaultOpen);
        comment.setEmail(comment.getEmail().trim());
        comment.setIp(IpAddressUtils.getIpAddress(request));
    }

    /**
     * URL合法性校验
     *
     * @param url url
     * @return 是否合法
     */
    private static boolean isValidUrl(String url) {
        return url.matches("^https?://([^!@#$%^&*?.\\s-]([^!@#$%^&*?.\\s]{0,63}[^!@#$%^&*?.\\s])?\\.)+[a-z]{2,6}/?");
    }

    /**
     * 对于昵称不是QQ号的评论，根据昵称Hash设置头像
     *
     * @param comment 当前收到的评论
     */
    private void setCommentRandomAvatar(Comment comment) {
        //设置随机头像
        //根据评论昵称取Hash，保证每一个昵称对应一个头像
        long nicknameHash = HashUtils.getMurmurHash32(comment.getNickname());
        //计算对应的头像
        long num = nicknameHash % 6 + 1;
        String avatar = "/img/comment-avatar/" + num + ".jpg";
        comment.setAvatar(avatar);
    }
}
