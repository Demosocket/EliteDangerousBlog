package com.demosocket.blog.dto;

import lombok.Data;
import lombok.Builder;

import java.util.List;

@Data
@Builder
public class SearchParametersDto {

    private List<String> tags;
    private Integer page;
    private Integer size;
    private String title;
    private Integer userId;
    private String sortField;
    private String order;
}
