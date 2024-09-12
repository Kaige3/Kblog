package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.SiteSetting;
import org.apache.ibatis.annotations.Mapper;


/**
 * (SiteSetting)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-08 14:27:01
 */
@Mapper
public interface SiteSettingMapper extends BaseMapper<SiteSetting> {

}
