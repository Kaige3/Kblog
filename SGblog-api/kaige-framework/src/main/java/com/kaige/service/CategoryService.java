package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.pojo.Category;
import com.kaige.entity.vo.CategoryVo;
import com.kaige.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-08-10 19:34:05
 */
@Service
public interface CategoryService extends IService<Category> {


    List<CategoryVo> listAllCategory();

    ResponseResult getCategoryList();
}

