package com.kaige.controller;

import com.kaige.entity.Result;
import com.kaige.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArchiveController {
    @Autowired
    private BlogService blogService;
    //按年月分组，统计公开博客数量
    @GetMapping("/archives")
    public Result archives(){
        return blogService.archives();
    }
}
