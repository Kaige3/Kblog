package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.pojo.Comment;
import org.mapstruct.Mapper;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-16 19:45:49
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
