package com.kaige.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaige.entity.Category;
import com.kaige.entity.PageResult;
import com.kaige.entity.Result;
import com.kaige.model.vo.BlogInfo;
import com.kaige.service.BlogService;
import com.kaige.service.CategoryService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 根据分类名，查询公开的文章列表
 */

@RestController
public class CategoryController {

    @Autowired
    private BlogService blogService;
    @GetMapping("/category")
    public Result category(@RequestParam String categoryName, @RequestParam(defaultValue = "1") Integer pageNum){
        PageResult<BlogInfo> blogInfoPageResult = blogService.getBloginfoListByCategoryNameAndIsPublished(categoryName,pageNum);
        return Result.ok("执行成功",blogInfoPageResult);
    }
}
