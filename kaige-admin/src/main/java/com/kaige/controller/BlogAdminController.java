package com.kaige.controller;

import com.kaige.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class BlogAdminController {

    @GetMapping("/blogs")
    public Result blogs(@RequestParam(defaultValue = "")String title,
                        @RequestParam(defaultValue = "")Integer categoryId,
                        @RequestParam(defaultValue = "1")Integer pageNum,
                        @RequestParam(defaultValue = "10")Integer pageSize){

    }
}
