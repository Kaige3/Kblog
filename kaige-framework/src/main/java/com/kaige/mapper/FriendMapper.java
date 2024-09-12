package com.kaige.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaige.entity.Friend;
import com.kaige.model.vo.FriendVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * (Friend)表数据库访问层
 *
 * @author makejava
 * @since 2024-09-10 10:52:00
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

    List<FriendVo> getFriendVoList();

    int updateViewsByNickname(String nickname);
}
