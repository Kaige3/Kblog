package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.Moment;

import java.util.List;


/**
 * (Moment)表服务接口
 *
 * @author makejava
 * @since 2024-09-10 12:47:20
 */
public interface MomentService extends IService<Moment> {

    List<Moment> getMomentVolist(Integer pageNum, boolean adminIdentity);

    void addLikeById(Long id);
}

