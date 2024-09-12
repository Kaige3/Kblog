package com.kaige.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.Friend;
import com.kaige.entity.SiteSetting;
import com.kaige.mapper.FriendMapper;
import com.kaige.model.vo.FriendInfo;
import com.kaige.model.vo.FriendVo;
import com.kaige.service.FriendService;
import com.kaige.service.SiteSettingService;
import com.kaige.utils.BeanCopyUtils;
import com.kaige.utils.markdown.MarkdownUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Friend)表服务实现类
 *
 * @author makejava
 * @since 2024-09-10 10:52:00
 */
@Service("friendService")
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private SiteSettingService siteSettingService;
    @Override
    public List<FriendVo> getFriendVoList() {
        List<FriendVo> friendVos =  friendMapper.getFriendVoList();
        return friendVos;
    }

    @Override
    public FriendInfo getFriendInfo(boolean cache, boolean md) {

        LambdaQueryWrapper<SiteSetting> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SiteSetting::getType,4);
        List<SiteSetting> list = siteSettingService.list(queryWrapper);
        FriendInfo friendInfo = new FriendInfo();
        list.stream().map(siteSetting-> {

            if ("friendContent".equals(siteSetting.getNameEn())) {
                if (md) {
                    friendInfo.setContent(MarkdownUtils.markdownToHtml(siteSetting.getValue()));
                } else {
                    friendInfo.setContent(siteSetting.getValue());
                }
            }
            else if("friendCommentEnabled".equals(siteSetting.getNameEn())){
                friendInfo.setCommentEnabled("1".equals(siteSetting.getValue()));
            }
            return friendInfo;
        }).collect(Collectors.toList());

        return friendInfo;
    }

    @Override
    public void updateViewsByNickname(String nickname) {
         if (friendMapper.updateViewsByNickname(nickname) != 1){
             throw new PersistenceException("操作失败");
         }
    }


}
