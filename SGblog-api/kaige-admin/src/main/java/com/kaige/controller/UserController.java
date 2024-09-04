package com.kaige.controller;

import com.alibaba.fastjson.JSON;
import com.kaige.entity.pojo.Role;
import com.kaige.entity.pojo.User;
import com.kaige.entity.pojo.UserInfoAndRoleIdsVo;
import com.kaige.entity.vo.PageVo;
import com.kaige.entity.vo.UserVo;
import com.kaige.result.ResponseResult;
import com.kaige.service.RoleService;
import com.kaige.service.UserService;
import com.kaige.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Slf4j
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult<PageVo> list( User user, Integer pageNum, Integer pageSize){
         return userService.selectAllUser(user,pageNum,pageSize);

    }
    @PostMapping
    public ResponseResult add(@Validated @RequestBody  User user){
        log.info("信息{}", JSON.toJSONString(user));
        return userService.addUser(user);
        }
//  删除用户
    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds ){
         if (userIds.contains(SecurityUtils.getUserId())){
             return ResponseResult.errorResult(500,"不能删除当前用户");
         }
         userService.removeByIds(userIds);
         return ResponseResult.okResult();
    }
//    用户修改数据回显
    @GetMapping(value = { "/{userId}" })
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId)
    {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,roles,roleIds);
        return ResponseResult.okResult(vo);
    }
//    修改用户
    @PutMapping
    public ResponseResult edit(@RequestBody User user){
        userService.updateUser(user);
        return ResponseResult.okResult();
    }
}

