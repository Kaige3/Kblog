package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.About;

import java.util.Map;


/**
 * (About)表服务接口
 *
 * @author makejava
 * @since 2024-09-09 14:40:55
 */
public interface AboutService extends IService<About> {


     Map<String, String> getAboutInfo();

    boolean getAboutCommentEnabled();

    void updateAboutInfo(Map<String, String> map);

    About findByNameEn(String key);

    Map<String, String> getAboutSetting();
}

