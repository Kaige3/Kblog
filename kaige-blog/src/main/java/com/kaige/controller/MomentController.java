package com.kaige.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaige.annotation.AccessLimit;
import com.kaige.constant.JwtConstants;
import com.kaige.entity.Moment;
import com.kaige.entity.PageResult;
import com.kaige.entity.Result;
import com.kaige.entity.User;
import com.kaige.service.MomentService;
import com.kaige.service.UserService;
import com.kaige.service.impl.UserServiceImpl;
import com.kaige.utils.JwtUtils;
import com.mysql.cj.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MomentController {
    @Autowired
    private MomentService momentService;
    @Autowired
    private UserServiceImpl userService;
    @GetMapping("/moments")
    @Transactional
    public Result moments(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestHeader(value = "Authorization",defaultValue = "") String jwt){
        boolean adminIdentity = false;
        if (JwtUtils.judgeTokenIsExist(jwt)){
            try {
                String subject = JwtUtils.getTokenBody(jwt).getSubject();
                if(subject.startsWith(JwtConstants.ADMIN_PREFIX)){
                    //这是博主
                    String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                    User admin = (User) userService.loadUserByUsername(username);
                    if (admin != null){
                        adminIdentity = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Moment> list = momentService.getMomentVolist(pageNum,adminIdentity);
        Page<Moment> page = new Page<>();
        page.setRecords(list);
        page.setTotal(list.size());
        PageResult<Moment> momentPageResult = new PageResult<>(page.getTotal(), page.getRecords());
        return Result.ok("获取成功",momentPageResult);
    }
    @AccessLimit(seconds = 86400,maxCount = 1,msg = "只能点赞一次")
    @PostMapping("/moment/like/{id}")
    public Result like(@PathVariable Long id){
        momentService.addLikeById(id);
        return Result.ok("点赞成功");
    }
}
