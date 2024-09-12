package com.kaige.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.constants.SystemConstants;
import com.kaige.entity.pojo.Link;
import com.kaige.entity.vo.LinkVo;
import com.kaige.mapper.LinkMapper;
import com.kaige.result.ResponseResult;
import com.kaige.service.LinkService;
import com.kaige.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2024-08-11 16:06:50
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getlink() {
        //      查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> list = list(queryWrapper);
//       转换为VO
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(list, LinkVo.class);

//        封装返回
        return ResponseResult.okResult(linkVos);
    }
}
