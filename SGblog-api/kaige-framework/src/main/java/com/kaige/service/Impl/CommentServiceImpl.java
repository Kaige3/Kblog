package com.kaige.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.constants.SystemConstants;
import com.kaige.entity.pojo.Comment;
import com.kaige.entity.pojo.User;
import com.kaige.entity.vo.CommentVo;
import com.kaige.entity.vo.PageVo;
import com.kaige.enums.AppHttpCodeEnum;
import com.kaige.exection.SystemException;
import com.kaige.mapper.CommentMapper;
import com.kaige.result.ResponseResult;
import com.kaige.service.CommentService;
import com.kaige.service.UserService;
import com.kaige.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-08-16 19:45:51
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {

//        查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
//        对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
//        根品论rootId位-1

        queryWrapper.eq(Comment::getRootId,-1);

//        评论类型
        queryWrapper.eq(Comment::getType,commentType);
//        分页查询

        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
//        查询所有根评论对应的之评论集合，并且赋值给对应的属性
        for (CommentVo commentVo: commentVoList){
//             查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));

    }

    @Override
    public ResponseResult addComment(Comment comment) {

        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }


    private List<CommentVo> getChildren(long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> commentVOs = list(queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(commentVOs);
        return commentVoList;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过createBy查询用户的昵称并赋值
            User user = userService.getById(commentVo.getCreateBy());
            if (user != null) {
                commentVo.setUsername(user.getNickName());
            } else {
                commentVo.setUsername("Unknown User"); // 或者其他默认值
            }

            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                User toCommentUser = userService.getById(commentVo.getToCommentUserId());
                if (toCommentUser != null) {
                    commentVo.setToCommentUserName(toCommentUser.getNickName());
                } else {
                    commentVo.setToCommentUserName("Unknown User"); // 或者其他默认值
                }
            }
        }
        return commentVos;
    }

}
