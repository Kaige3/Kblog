package com.kaige.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaige.constant.JwtConstants;
import com.kaige.entity.Result;
import com.kaige.entity.User;
import com.kaige.model.vo.LoginInfo;
import com.kaige.service.UserService;
import com.kaige.utils.JwtUtils;
import com.kaige.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    /*@PostMapping("/login")
    public Result login(@RequestBody LoginInfo loginInfo){

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,loginInfo.getUsername());
        queryWrapper.eq(User::getPassword,loginInfo.getPassword());
        User user = userService.getOne(queryWrapper);
        if(!"ROLE_ADMIN".equals(user.getRole())){
            return Result.create(403,"无权限");
        }
        user.setPassword(null);
        String token = JwtUtils.generateToken(JwtConstants.ADMIN_PREFIX + user.getUsername());
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("user",user);
        map.put("token",token);
        return Result.ok("登录成功",map);
    }*/
    @PostMapping("/login")
    public Result login(@RequestBody LoginInfo loginInfo){

        // 根据用户名和密码查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginInfo.getUsername());
        User user = userService.getOne(queryWrapper);
        if (user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        boolean password = HashUtils.matchBC(loginInfo.getPassword(), user.getPassword());
        if (!password){
            throw new UsernameNotFoundException("密码错误");
        }

        if (!"ROLE_admin".equals(user.getRole())) {
            return Result.create(403, "无权限");
        }
        user.setPassword(null);
        String jwt = JwtUtils.generateToken(JwtConstants.ADMIN_PREFIX + user.getUsername());
        Map<String, Object> map = new HashMap<>(4);
        map.put("user", user);
        map.put("token", jwt);
        return Result.ok("登录成功", map);
    }

}
