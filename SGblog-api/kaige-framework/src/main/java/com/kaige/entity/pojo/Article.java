package com.kaige.entity.pojo;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 文章表(Article)表实体类
 *
 * @author makejava
 * @since 2024-08-10 16:18:42
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_article")
@Accessors(chain = true)
public class Article extends Model<Article> {

    @TableId
    private Long id;
//标题
    private String title;
//文章内容
    private String content;
//文章摘要
    private String summary;
//所属分类id
//    分类名称
@TableField(exist = false)
    private String categoryName;
    private Long categoryId;
//缩略图
    private String thumbnail;
//是否置顶（0否，1是）
    private String isTop;
//状态（0已发布，1草稿）
    private String status;
//访问量
    private Long viewCount;
//是否允许评论 1是，0否
    private String isComment;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
//删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

    public Article(Long id, long viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }

}

