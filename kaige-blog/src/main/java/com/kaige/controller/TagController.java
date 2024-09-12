package com.kaige.controller;

import com.kaige.entity.PageResult;
import com.kaige.entity.Result;
import com.kaige.model.vo.BlogInfo;
import com.kaige.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {
    @Autowired
    private BlogService blogService;
    @GetMapping("/tag")
    public Result category(@RequestParam String tagName, @RequestParam(defaultValue = "1") Integer pageNum){
        PageResult<BlogInfo> blogInfoPageResult = blogService.getBloginfoListByTagNameAndIsPublished(tagName,pageNum);
        return Result.ok("执行成功",blogInfoPageResult);
    }
}
