package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.dto.AddArticleDto;
import com.kaige.entity.pojo.Article;
import com.kaige.entity.vo.AdminArticleDetailVo;
import com.kaige.entity.vo.BlogDataVO;
import com.kaige.entity.vo.PageVo;
import com.kaige.result.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    PageVo selectArtclePage(Integer pageNum, Integer pageSize,Article article);

    AdminArticleDetailVo selectById(Long id);

    ResponseResult archives();
}
