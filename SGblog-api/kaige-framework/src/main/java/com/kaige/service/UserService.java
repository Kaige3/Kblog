package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.pojo.User;
import com.kaige.entity.vo.PageVo;
import com.kaige.entity.vo.UserVo;
import com.kaige.result.ResponseResult;

import java.util.List;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-08-16 21:11:53
 */
public interface UserService extends IService<User> {

    ResponseResult userinfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult<PageVo> selectAllUser(User user, Integer pageNum, Integer pageSize);

    ResponseResult addUser(User user);

    void updateUser(User user);
}

