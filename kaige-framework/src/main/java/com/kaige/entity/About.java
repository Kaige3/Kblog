package com.kaige.entity;


import java.io.Serializable;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (About)表实体类
 *
 * @author makejava
 * @since 2024-09-09 14:40:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("about")
public class About  {

    @TableId
    private Long id;


    private String nameEn;

    private String nameZh;

    private String value;
}

