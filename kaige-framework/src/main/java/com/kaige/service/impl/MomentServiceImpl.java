package com.kaige.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.Moment;
import com.kaige.entity.PageResult;
import com.kaige.mapper.MomentMapper;
import com.kaige.service.MomentService;
import com.kaige.utils.markdown.MarkdownUtils;
//import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Moment)表服务实现类
 *
 * @author makejava
 * @since 2024-09-10 12:47:20
 */
@Service("momentService")
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {

    @Autowired
    private MomentService momentService;
    @Autowired
    private MomentMapper momentMapper;
    private static final int pageSize = 5;
    //动态列表排序方式
    private static final String orderBy = "create_time desc";
    private static final String PRIVATE_MOMENT_CONTENT = "<p>此条为私密动态，仅发布者可见！</p>";
    @Override
    public List<Moment> getMomentVolist(Integer pageNum, boolean adminIdentity) {

        LambdaQueryWrapper<Moment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Moment::getCreateTime);
        Page<Moment> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        List<Moment> records = page.getRecords();

        records.stream().peek(list->{
            if (adminIdentity || list.getPublished()){
                list.setContent(MarkdownUtils.markdownToHtml(list.getContent()));
            }else {
                list.setContent(PRIVATE_MOMENT_CONTENT);
            }
        }).collect(Collectors.toList());
//        PageResult<Moment> result = new PageResult<>(page.getTotal(),page.getRecords());
        return records;
    }

    @Override
    public void addLikeById(Long id) {
        momentMapper.addLikeById(id);
    }
}
