package com.kaige.controller;

import com.kaige.entity.pojo.User;
import com.kaige.result.ResponseResult;
import com.kaige.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userinfo(){
        return userService.userinfo();
    }

    @PutMapping("/userInfo")

    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@Validated @RequestBody User user){
        return userService.register(user);
    }
}
