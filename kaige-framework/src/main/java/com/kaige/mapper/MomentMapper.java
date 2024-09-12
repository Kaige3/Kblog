package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.Moment;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Moment)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-10 12:47:20
 */
@Mapper
public interface MomentMapper extends BaseMapper<Moment> {

    void addLikeById(Long id);
}
