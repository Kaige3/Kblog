package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.LoginLog;


/**
 * (LoginLog)表服务接口
 *
 * @author makejava
 * @since 2024-09-07 17:09:19
 */
public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog(LoginLog log);
}

