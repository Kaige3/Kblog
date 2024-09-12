package com.kaige.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.About;
import com.kaige.mapper.AboutMapper;
import com.kaige.service.AboutService;
import com.kaige.service.RedisService;
import com.kaige.utils.markdown.MarkdownUtils;
import org.apache.poi.poifs.crypt.dsig.services.RevocationDataService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * (About)表服务实现类
 *
 * @author makejava
 * @since 2024-09-09 14:40:55
 */
@Service("aboutService")
public class AboutServiceImpl extends ServiceImpl<AboutMapper, About> implements AboutService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AboutService aboutService;
    @Override
    public Map<String, String> getAboutInfo() {

        String redisKey = RedisKeyConstants.ABOUT_INFO_MAP;
        Map<String, String> mapByValue = redisService.getMapByValue(redisKey);
        if (mapByValue!=null){
            return mapByValue;
        }
        List<About> list = list();
        HashMap<String, String> map = new HashMap<>(16);
        for (About about : list){
            if("content".equals(about.getNameEn())){
                about.setValue(MarkdownUtils.markdownToHtmlExtensions(about.getValue()));
            }
            map.put(about.getNameEn(),about.getValue());
        }
        redisService.saveMapToValue(redisKey,map);
        return map;
    }

    @Override
    public boolean getAboutCommentEnabled() {
        LambdaQueryWrapper<About> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(About::getNameEn,"commentEnabled");
        About one = getOne(queryWrapper);
        return one != null;
    }

    @Override
    public void updateAboutInfo(Map<String, String> map) {
        Set<String> keySets = map.keySet();
        keySets.forEach(key ->{
            About about =  aboutService.findByNameEn(key);
            if (about != null){
                about.setValue(map.get(key));
                updateById(about);
            }
        });
        String redisKey = RedisKeyConstants.ABOUT_INFO_MAP;
        redisService.deleteByKey(redisKey);
        redisService.saveMapToValue(redisKey,map);
    }

    @Override
    public About findByNameEn(String key) {
        LambdaQueryWrapper<About> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(About::getNameEn,key);
        return getOne(queryWrapper);
    }

    @Override
    public Map<String, String> getAboutSetting() {
        List<About> lists = list();
        HashMap<String, String> map = new HashMap<>();
        lists.forEach( about->{
            map.put(about.getNameEn(),about.getValue());
        }
        );
        return map;
    }
}
