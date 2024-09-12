package com.kaige.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Blog)表实体类
 *
 * @author makejava
 * @since 2024-09-06 13:53:58
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog")
public class Blog  {
@TableId
    private Long id;

//文章标题
    private String title;
//文章首图，用于随机文章展示
    private String firstPicture;
//文章正文
    private String content;
//描述
    private String description;
//公开或私密

    private Boolean isPublished;
//推荐开关
    private Boolean isRecommend;
//赞赏开关
//    @TableField("appreciation")
    private Boolean isAppreciation;
//评论开关
//    @TableField("commentEnabled")
    private Boolean isCommentEnabled;
//创建时间
    private Date createTime;
//更新时间
    private Date updateTime;
//浏览次数
    private Integer views;
//文章字数
    private Integer words;
//阅读时长(分钟)
    private Integer readTime;
//文章分类
    private Long categoryId;
//是否置顶
//    @TableField("top")
    private Boolean isTop;
//密码保护
    private String password;
//文章作者
    private Long userId;

//    private User user;//文章作者(因为是个人博客，也可以不加作者字段，暂且加上)
//    private Category category;//文章分类
//    private List<Tag> tags = new ArrayList<>();//文章标签
}

