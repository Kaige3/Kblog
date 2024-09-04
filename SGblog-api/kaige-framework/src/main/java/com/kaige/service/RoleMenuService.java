package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.pojo.RoleMenu;
import org.springframework.stereotype.Service;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2024-08-28 21:24:17
 */
@Service
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);
}

