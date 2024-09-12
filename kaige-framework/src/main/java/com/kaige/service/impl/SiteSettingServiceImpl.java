package com.kaige.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.constant.RedisKeyConstants;
import com.kaige.constant.SiteSettingConstants;
import com.kaige.entity.SiteSetting;
import com.kaige.mapper.SiteSettingMapper;
import com.kaige.model.vo.Badge;
import com.kaige.model.vo.Copyright;
import com.kaige.model.vo.Favorite;
import com.kaige.model.vo.Introduction;
import com.kaige.service.RedisService;
import com.kaige.service.SiteSettingService;
import com.kaige.utils.JacksonUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (SiteSetting)表服务实现类
 *
 * @author makejava
 * @since 2024-09-08 14:27:02
 */
@Service("siteSettingService")
public class SiteSettingServiceImpl extends ServiceImpl<SiteSettingMapper, SiteSetting> implements SiteSettingService {


    @Autowired
    private RedisService redisService;
    private static final Pattern PATTERN = Pattern.compile("\"(.*?)\"");
    @Override
    public Map<String, Object> getSiteInfo() {
//      从redis中获取数据
        String redisKey = RedisKeyConstants.SITE_INFO_MAP;
        Map<String,Object> siteInfoMapFromRedis = redisService.getMapByValue(redisKey);
        if(siteInfoMapFromRedis != null){
            return siteInfoMapFromRedis;
        }

        List<SiteSetting> SiteSettingslist = list();

        HashMap<String, Object> siteInfo = new HashMap<>();
        List<Badge> badges = new ArrayList<>();
        Introduction introduction = new Introduction();
        List<Favorite> favorites = new ArrayList<>();
        List<String> rollTexts = new ArrayList<>();

        for (SiteSetting s:SiteSettingslist){
            switch (s.getType()){
                case 1:
                    if(SiteSettingConstants.COPYRIGHT.equals(s.getNameEn())){
                        Copyright copyright = JacksonUtils.readValue(s.getValue(), Copyright.class);
                        siteInfo.put(s.getNameZh(),copyright);
                    }else {
                        siteInfo.put(s.getNameEn(),s.getValue());
                    }
                    break;
                case 2:
                    switch (s.getNameEn()){
                        case SiteSettingConstants.AVATAR:
                            introduction.setAvatar(s.getValue());
                            break;
                        case SiteSettingConstants.NAME:
                            introduction.setName(s.getValue());
                            break;
                        case SiteSettingConstants.GITHUB:
                            introduction.setGithub(s.getValue());
                            break;
                        case SiteSettingConstants.TELEGRAM:
                            introduction.setTelegram(s.getValue());
                            break;
                        case SiteSettingConstants.QQ:
                            introduction.setQq(s.getValue());
                            break;
                        case SiteSettingConstants.BILIBILI:
                            introduction.setBilibili(s.getValue());
                            break;
                        case SiteSettingConstants.NETEASE:
                            introduction.setNetease(s.getValue());
                            break;
                        case SiteSettingConstants.EMAIL:
                            introduction.setEmail(s.getValue());
                            break;
                        case SiteSettingConstants.FAVORITE:
                            Favorite favorite = JacksonUtils.readValue(s.getValue(), Favorite.class);
                            favorites.add(favorite);
                            break;
                        case SiteSettingConstants.ROLL_TEXT:
                            Matcher matches = PATTERN.matcher(s.getValue());
                            while (matches.find()){
                                rollTexts.add(matches.group(1));
                            }
                            break;
                    }
                case 3:
                    Badge badge = JacksonUtils.readValue(s.getValue(), Badge.class);
                    badges.add(badge);
                    break;
                default:
                    break;
            }
        }
        introduction.setFavorites(favorites);
        introduction.setRollText(rollTexts);
        Map<String, Object> map = new HashMap<>(8);
        map.put("introduction",introduction);
        map.put("siteInfo",siteInfo);
        map.put("badges",badges);
        redisService.saveMapToValue(redisKey,map);
        return map;
    }
}
