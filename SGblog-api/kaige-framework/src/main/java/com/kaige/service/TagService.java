package com.kaige.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaige.entity.dto.TagListDto;
import com.kaige.entity.pojo.Tag;
import com.kaige.entity.vo.PageVo;
import com.kaige.entity.vo.TagVo;
import com.kaige.result.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 标签(SgTag)表服务接口
 *
 * @author makejava
 * @since 2024-08-23 19:52:02
 */
@Service
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    List<TagVo> listAllTag();
}

