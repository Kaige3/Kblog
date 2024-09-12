package com.kaige.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.BlogTag;
import com.kaige.mapper.BlogTagMapper;
import com.kaige.service.BlogTagService;
import org.springframework.stereotype.Service;

/**
 * (BlogTag)表服务实现类
 *
 * @author makejava
 * @since 2024-09-09 23:32:14
 */
@Service("blogTagService")
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagService {

}
