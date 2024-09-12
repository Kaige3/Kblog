package com.kaige.entity.dto;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ListTagDto {
    List<TagListDto> tagDtos;
}
