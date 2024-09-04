package com.kaige.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kaige.constants.SystemConstants;
import com.kaige.entity.dto.ChangStatusDto;
import com.kaige.entity.pojo.Role;
import com.kaige.entity.pojo.User;
import com.kaige.entity.vo.RoleVo;
import com.kaige.result.ResponseResult;
import com.kaige.service.RoleService;
import com.kaige.service.UserService;
import com.kaige.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class UserRoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
//    角色列表
    @GetMapping("/list")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize) {
        return roleService.selectRolePage(role,pageNum,pageSize);
    }
//    改变角色状态
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangStatusDto changStatusDto){
        Role role = new Role();
        role.setId(changStatusDto.getRoleId());
        role.setStatus(changStatusDto.getStatus());
        return ResponseResult.okResult(roleService.updateById(role));
    }
    /**
     * 修改保存角色
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Role role)
    {
        roleService.updateRole(role);
        return ResponseResult.okResult();
    }
    /**
     * 新增角色
     */
    @PostMapping
    public ResponseResult add( @RequestBody Role role)
    {
        roleService.insertRole(role);
        return ResponseResult.okResult();

    }
    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/{roleId}")
    public ResponseResult getInfo(@PathVariable Long roleId)
    {
        Role role = roleService.getById(roleId);
        return ResponseResult.okResult(role);
    }
    /**
     * 根据id删除角色
     */
     @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
         roleService.removeById(id);
         return ResponseResult.okResult();
     }

    /**
     * 新增用户，需要查询角色列表接口
     */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = roleService.list(queryWrapper);
        return ResponseResult.okResult(list);

    }

}
