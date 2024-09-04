package com.kaige.controller;

import com.kaige.result.ResponseResult;
import com.kaige.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

//   @GetMapping("list")
//    public List<Article> test(){
//        return articleService.list();
//    }
    @GetMapping("hotArticleList")
    public ResponseResult hotArticleList(){
       return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    public ResponseResult article(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id")Long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

    //获取文章归档
    @GetMapping("/archives")
    public ResponseResult archives(){
      return articleService.archives();
    }
}
