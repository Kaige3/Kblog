package com.kaige.service.Impl;

import com.kaige.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
//    判断权限
    public boolean hasPermission(String permission){
//        如果是超级管理员，直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
//        else 返回当前登录用户的所具有的权限列表，判断是否有permission存在
        List<String> permissions = SecurityUtils.getLoginUser().getPermission();
        return permissions.contains(permission);
    }
}
