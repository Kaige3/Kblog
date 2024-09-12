package com.kaige.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 分页结果
 * @Date: 2020-08-08
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PageResult<T> {
    private Long totalPage;//总页数
    private List<T> list;//数据

    public PageResult(Long totalPage, List<T> list) {
        this.totalPage = totalPage;
        this.list = list;
    }
}
