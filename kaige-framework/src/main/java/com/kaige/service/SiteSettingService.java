package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.SiteSetting;

import java.util.Map;


/**
 * (SiteSetting)表服务接口
 *
 * @author makejava
 * @since 2024-09-08 14:27:02
 */
public interface SiteSettingService extends IService<SiteSetting> {

    Map<String, Object> getSiteInfo();
}

