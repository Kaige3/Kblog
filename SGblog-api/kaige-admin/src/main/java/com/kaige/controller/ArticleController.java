package com.kaige.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaige.entity.dto.AddArticleDto;
import com.kaige.entity.pojo.Article;
import com.kaige.entity.pojo.ArticleTag;
import com.kaige.entity.vo.AdminArticleDetailVo;
import com.kaige.entity.vo.PageVo;
import com.kaige.result.ResponseResult;
import com.kaige.service.ArticleService;
import com.kaige.service.ArticleTagService;
import com.kaige.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.poi.hssf.usermodel.HeaderFooter.page;


@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
         return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,Article article){
        PageVo pageVo = articleService.selectArtclePage(pageNum,pageSize,article);
        return ResponseResult.okResult(pageVo);
    }
//    数据回显
    @GetMapping("/{id}")
    public  ResponseResult selectById(@PathVariable(value = "id") Long id){
        AdminArticleDetailVo adminArticleDetailVo =  articleService.selectById(id);
        return ResponseResult.okResult(adminArticleDetailVo);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody AddArticleDto AddArticleDto){
        Article article = BeanCopyUtils.copyBean(AddArticleDto, Article.class);
        articleService.updateById(article);
//        删除原有的和博客关联的标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(queryWrapper);
//        添加新的和博客关联的标签
        List<ArticleTag> articleTags = AddArticleDto.getTags().stream()
                .map(tagId -> new ArticleTag(AddArticleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable Long id){
        articleService.removeById(id);
//        删除原有的和博客关联的标签，
        return ResponseResult.okResult();
    }

}
