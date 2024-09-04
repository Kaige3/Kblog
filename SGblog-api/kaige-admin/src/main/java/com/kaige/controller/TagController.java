package com.kaige.controller;

import com.kaige.entity.dto.TagDto;
import com.kaige.entity.dto.TagListDto;
import com.kaige.entity.pojo.Tag;
import com.kaige.entity.vo.PageVo;
import com.kaige.entity.vo.TagVo;
import com.kaige.result.ResponseResult;
import com.kaige.service.TagService;
import com.kaige.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult add(@RequestBody TagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
         tagService.save(tag);
         return ResponseResult.okResult();
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteById(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }
//    数据回显
    @GetMapping(value = "/{id}")
    public ResponseResult selectById(@PathVariable(value = "id") Long id){
        Tag byId = tagService.getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(byId, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @PutMapping
    public ResponseResult update(@RequestBody TagVo tagvo){
        Tag tag = BeanCopyUtils.copyBean(tagvo, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list =  tagService.listAllTag();
        return ResponseResult.okResult(list);
    }


}
