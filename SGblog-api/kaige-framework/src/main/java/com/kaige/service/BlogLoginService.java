package com.kaige.service;

import com.kaige.entity.pojo.User;
import com.kaige.result.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();

}
