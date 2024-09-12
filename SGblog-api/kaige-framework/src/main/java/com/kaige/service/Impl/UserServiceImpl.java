package com.kaige.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.pojo.User;
import com.kaige.entity.pojo.UserRole;
import com.kaige.entity.vo.PageVo;
import com.kaige.entity.vo.UserInfoVo;
import com.kaige.entity.vo.UserVo;
import com.kaige.enums.AppHttpCodeEnum;
import com.kaige.exection.SystemException;
import com.kaige.mapper.UserMapper;
import com.kaige.result.ResponseResult;
import com.kaige.service.UserRoleService;
import com.kaige.service.UserService;
import com.kaige.utils.BeanCopyUtils;
import com.kaige.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-08-16 21:11:53
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult userinfo() {
//        获取当前用户id
        Long userId = SecurityUtils.getUserId();
//        更具用户id查询用户信息
        User user = getById(userId);
//
//       封装VO
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {

        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
    if(userNameExist(user.getUserName())){
        throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
    }
    if(NickNameExist((user.getNickName()))){
        throw new SystemException(AppHttpCodeEnum.USERNICKNAME_NOT_NULL);
    }
//    对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> selectAllUser(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(user.getUserName()),User::getUserName,user.getUserName());
        queryWrapper.like(StringUtils.hasText(user.getPhonenumber()),User::getPhonenumber,user.getPhonenumber());
        queryWrapper.like(StringUtils.hasText(user.getStatus()),User::getStatus,user.getStatus());
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
//        获取插叙的目标用户集合
        List<User> records = page.getRecords();
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(records);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    public void updateUser(User user) {
//        先删除用户与角色关联
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(queryWrapper);
//        新增用户与角色关联
        insertUserRole(user);
        updateById(user);
    }

    @Autowired
    private UserRoleService userRoleService;
        private void insertUserRole(User user) {
            List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                    .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
            userRoleService.saveBatch(sysUserRoles);
        }
    private boolean userNameExist(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        return count(queryWrapper)>0;
    }
    private boolean NickNameExist(String nickname){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickname);
        return count(queryWrapper)>0;
    }
}


