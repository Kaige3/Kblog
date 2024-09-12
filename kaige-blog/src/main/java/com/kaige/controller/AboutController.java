package com.kaige.controller;

import com.kaige.entity.About;
import com.kaige.entity.Result;
import com.kaige.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AboutController {
    @Autowired
    private AboutService aboutService;
    @GetMapping("/about")
    public Result about(){
        Map<String,String> map =  aboutService.getAboutInfo();
        return Result.ok("ok",map);
    }
}
