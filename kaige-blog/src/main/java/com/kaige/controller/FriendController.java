package com.kaige.controller;

import com.kaige.entity.Result;
import com.kaige.model.vo.FriendInfo;
import com.kaige.model.vo.FriendVo;
import com.kaige.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;
    @GetMapping("/friends")
    public Result friends(){
        List<FriendVo> friendVos =  friendService.getFriendVoList();
        FriendInfo friendInfo =  friendService.getFriendInfo(true,true);
        HashMap<String, Object> map = new HashMap<>();
        map.put("friendList",friendVos);
        map.put("friendInfo",friendInfo);
        return Result.ok("获取成功",map);
    }
    /**
     * 更新友联浏览次数
     */
    @PostMapping("/friend")
    public Result addViews(@RequestParam String nickname){
        friendService.updateViewsByNickname(nickname);
        return Result.ok("操作成功");
    }
}
