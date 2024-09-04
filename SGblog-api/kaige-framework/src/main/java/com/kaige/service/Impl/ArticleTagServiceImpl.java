package com.kaige.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.pojo.ArticleTag;
import com.kaige.mapper.ArticleTagMapper;
import com.kaige.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2024-08-25 18:48:24
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
