package com.kaige.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (BlogTag)表实体类
 *
 * @author makejava
 * @since 2024-09-09 23:32:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_tag")
public class BlogTag  {


    private Long blogId;

    private Long tagId;
}

