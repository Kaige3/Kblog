package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.pojo.Menu;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2024-08-25 13:28:49
 */
@Service
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);


    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> selectMenuList(Menu menu);

    List<Long> selectMenuListByRoleId(Long roleId);
}

