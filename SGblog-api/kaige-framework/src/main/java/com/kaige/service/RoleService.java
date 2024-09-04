package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.pojo.Role;
import com.kaige.entity.pojo.User;
import com.kaige.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-08-25 13:28:50
 */
@Service
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);


    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);

    void updateRole(Role role);

    void insertRole(Role role);

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);
}

