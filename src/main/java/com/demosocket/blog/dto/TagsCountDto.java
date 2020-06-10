package com.demosocket.blog.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class TagsCountDto {

    private String tag;
    private Integer post_count;
}
