package com.kaige.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.constants.SystemConstants;
import com.kaige.entity.pojo.Menu;
import com.kaige.service.MenuService;
import com.kaige.mapper.MenuMapper;
import com.kaige.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-08-25 13:28:50
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
//        如果是管理员，返回所有的权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> list = list(queryWrapper);
            List<String> perms = list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        return getBaseMapper().selectPermsByUserId(id);




    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
//       判断是管理员
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        if(SecurityUtils.isAdmin()){
            menus = menuMapper.selectAllRouterMenu();
        }else{
            Object ments = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
//       1 获取符合要求的所有Menu
//       0 获取当前用户的Menu
//        构建tree
//        先找出第一层的菜单，然后去找他们的子菜单，设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;

    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.like(StringUtils.hasText(menu.getStatus()),Menu::getMenuName,menu.getStatus());
//        排序
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        return list;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }

    private List<Menu> builderMenuTree(List<Menu> menus,Long parentId){
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
