package com.demosocket.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagsCountDto {

    private String tag;
    private Integer post_count;
}
