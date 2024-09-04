package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.pojo.Comment;
import com.kaige.result.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}