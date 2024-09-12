package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.Tag;

import java.util.List;


/**
 * (Tag)表服务接口
 *
 * @author makejava
 * @since 2024-09-06 21:59:47
 */
public interface TagService extends IService<Tag> {

    List<Tag> getTagsByBlogId(Long id);

    List<Tag> getTagListNotId();
}

