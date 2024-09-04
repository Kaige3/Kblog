package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.pojo.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-02-03 16:25:39
 */
@Mapper
//@Repository(value = "UserMapper")
public interface UserMapper extends BaseMapper<User> {

}

