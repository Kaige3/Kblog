package com.kaige.controller;

import com.kaige.constants.SystemConstants;
import com.kaige.entity.dto.AddCommentDto;
import com.kaige.entity.pojo.Comment;
import com.kaige.result.ResponseResult;
import com.kaige.service.CommentService;
import com.kaige.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static net.sf.jsqlparser.parser.feature.Feature.comment;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment1 = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment1);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友联评论列表",notes = "获取一条友联评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })

    public ResponseResult LinkcommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
