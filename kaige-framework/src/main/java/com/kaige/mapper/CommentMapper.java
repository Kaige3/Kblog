package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.Comment;
import com.kaige.model.vo.PageComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-10 18:51:52
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    Integer countByPageAndIsPublished(@Param("page") Integer page, @Param("blogId") Long blogId, @Param("isPublished") Boolean isPublished);

    List<PageComment> getPageCommentListByPageAndParentCommentId(@Param("page")Integer page, @Param("blogId")Long blogId, @Param("parentCommentId") long parentCommentId);

    int saveComment(Comment comment);

    Comment getCommentById(Long id);
}
