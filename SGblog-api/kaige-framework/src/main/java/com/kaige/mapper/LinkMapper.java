package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.pojo.Link;
import org.mapstruct.Mapper;


/**
 * 友链(SgLink)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-11 15:51:18
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}
