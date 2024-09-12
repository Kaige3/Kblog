package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;


/**
 * (LoginLog)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-07 17:09:19
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    default int saveLoginLog(LoginLog log) {
        return 0;
    }
}
