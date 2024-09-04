package com.kaige.controller;

import com.kaige.entity.pojo.LoginUser;
import com.kaige.entity.pojo.Menu;
import com.kaige.entity.pojo.User;
import com.kaige.entity.vo.AdminUserInfoVo;
import com.kaige.entity.vo.RoutersVo;
import com.kaige.entity.vo.UserInfoVo;
import com.kaige.enums.AppHttpCodeEnum;
import com.kaige.exection.SystemException;
import com.kaige.result.ResponseResult;
import com.kaige.service.LoginService;
import com.kaige.service.MenuService;
import com.kaige.service.RoleService;
import com.kaige.utils.BeanCopyUtils;
import com.kaige.utils.RedisCache;
import com.kaige.utils.SecurityUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisCache redisCache;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
//       获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
//       根据用户id查询权限信息
        List<String> perms =  menuService.selectPermsByUserId(loginUser.getUser().getId());
//        根据用户id查询角色信息
        List<String> rolrList =  roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
//        获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
//        封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, rolrList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
//        查询menu ,结果是tree的形式
        List<Menu> menus =  menuService.selectRouterMenuTreeByUserId(userId);
//        封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login"+userId);
        return ResponseResult.okResult();
    }

}
