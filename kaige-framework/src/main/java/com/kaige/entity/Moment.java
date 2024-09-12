package com.kaige.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Moment)表实体类
 *
 * @author makejava
 * @since 2024-09-10 12:47:20
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("moment")
public class Moment  {
@TableId
    private Long id;

//动态内容
    private String content;
//创建时间
    private Date createTime;
//点赞数量
    private Integer likes;
//是否公开
    @TableField("is_published")
    private Boolean published;
}

