package com.kaige.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.pojo.UserRole;
import com.kaige.mapper.UserRoleMapper;
import com.kaige.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2024-08-29 15:12:45
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
