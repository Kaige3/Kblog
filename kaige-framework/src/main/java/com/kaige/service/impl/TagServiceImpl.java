package com.kaige.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.Tag;
import com.kaige.mapper.TagMapper;
import com.kaige.service.TagService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Tag)表服务实现类
 *
 * @author makejava
 * @since 2024-09-06 21:59:47
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<Tag> getTagsByBlogId(Long id) {
       return tagMapper.getTagsByBlogId(id);
    }

    @Override
    public List<Tag> getTagListNotId() {
        return tagMapper.selectNotId();
    }
}
