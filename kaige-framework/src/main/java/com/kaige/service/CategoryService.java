package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.Category;

import java.util.List;


/**
 * (Category)表服务接口
 *
 * @author makejava
 * @since 2024-09-06 21:52:04
 */
public interface CategoryService extends IService<Category> {
    List<Category> getCategoryNameList();



}

