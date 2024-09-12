package com.kaige.controller;

import com.kaige.entity.Result;
import com.kaige.entity.User;
import com.kaige.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 账号密码修改
 */
@RestController
@RequestMapping("/admin")
public class AccountController {

    @Autowired
    private UserService userService;
    @PostMapping("account")
    public Result account(@RequestBody User user, @RequestHeader(value = "Authorization",defaultValue = "")String jwt){
        Boolean ref = userService.changeAccount(user,jwt);
        return ref ? Result.ok("修改成功"):Result.error("修改失败");
    }
}
