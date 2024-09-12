package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.pojo.Role;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-25 13:28:50
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectByKeyUserId(Long id);

    List<Long> selectRolebyUserId(Long userId);
}
