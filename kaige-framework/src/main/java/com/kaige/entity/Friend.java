package com.kaige.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Friend)表实体类
 *
 * @author makejava
 * @since 2024-09-10 10:52:00
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("friend")
public class Friend  {
@TableId
    private Long id;

//昵称
    private String nickname;
//描述
    private String description;
//站点
    private String website;
//头像
    private String avatar;
//公开或隐藏
    private Boolean isPublished;
//点击次数
    private Integer views;
//创建时间
    private Date createTime;
}

