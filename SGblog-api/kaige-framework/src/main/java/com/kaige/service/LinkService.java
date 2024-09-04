package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.pojo.Link;
import com.kaige.result.ResponseResult;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-08-11 16:06:50
 */
public interface LinkService extends IService<Link> {

    ResponseResult getlink();
}

