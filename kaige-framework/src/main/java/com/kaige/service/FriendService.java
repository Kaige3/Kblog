package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.Friend;
import com.kaige.model.vo.FriendInfo;
import com.kaige.model.vo.FriendVo;

import java.util.List;


/**
 * (Friend)表服务接口
 *
 * @author makejava
 * @since 2024-09-10 10:52:00
 */
public interface FriendService extends IService<Friend> {

    List<FriendVo> getFriendVoList();


    FriendInfo getFriendInfo(boolean cache, boolean md);

    void updateViewsByNickname(String nickname);
}

