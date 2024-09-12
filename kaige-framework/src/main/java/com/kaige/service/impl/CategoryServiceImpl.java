package com.kaige.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.Category;
import com.kaige.mapper.CategoryMapper;
import com.kaige.service.CategoryService;
import com.kaige.utils.BeanCopyUtils;
import kotlin.jvm.internal.Lambda;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Category)表服务实现类
 *
 * @author makejava
 * @since 2024-09-06 21:52:04
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> getCategoryNameList() {
        return categoryMapper.getCategoryNameList();

    }
}
