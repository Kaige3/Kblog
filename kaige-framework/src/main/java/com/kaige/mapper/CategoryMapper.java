package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * (Category)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-06 21:52:03
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> getCategoryNameList();
}
