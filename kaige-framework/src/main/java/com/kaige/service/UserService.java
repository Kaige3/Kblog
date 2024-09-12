package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.User;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2024-09-07 14:49:53
 */
public interface UserService extends IService<User> {

    Boolean changeAccount(User user, String jwt);
}

