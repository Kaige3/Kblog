package com.kaige.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (SiteSetting)表实体类
 *
 * @author makejava
 * @since 2024-09-08 14:27:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("site_setting")
public class SiteSetting  {
@TableId
    private Long id;


    private String nameEn;

    private String nameZh;

    private String value;
//1基础设置，2页脚徽标，3资料卡，4友链信息
    private Integer type;
}

