package com.kaige.controller;

import com.kaige.entity.Result;
import com.kaige.service.AboutService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("admin")
public class AboutAdminController {

    @Autowired
    private AboutService aboutService;

    /**
     * 关于我 数据回显
     * @return
     */
    @GetMapping("/about")
    public Result about(){
        Map<String, String> aboutInfo = aboutService.getAboutSetting();
        return Result.ok("获取成功",aboutInfo);
    }

    /**
     * 修改我的页面
     * @param map
     * @return
     */
    @PutMapping("/about")
    public Result updateAboutInfo(@RequestBody Map<String,String> map){
        aboutService.updateAboutInfo(map);
        return Result.ok("执行成功");
    }
}
