package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.Comment;
import com.kaige.model.vo.PageComment;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * (Comment)表服务接口
 *
 * @author makejava
 * @since 2024-09-10 18:51:53
 */
public interface CommentService extends IService<Comment> {

    int countByPageAndIsPublished(Integer page, Long blogId,Boolean isPublished);

    List<PageComment> getPageCommentList(Integer page, Long blogId, long parentCommentId);

    Comment getCommentById(Long parentCommentId);

    void saveComment(Comment comment);
}

