package com.springboot.blogapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {

    private Long id;
    @NotEmpty
    @Size(min = 2,message = "title should have at least 2 character")
    private String title;

    @NotEmpty
    @Size(min = 10,message = "description should have at least 10 character")
    private String description;

    @NotEmpty
    private String content;
    //Default class modelmapper work here it automatically fetch all comments of particular post
    private Set<CommentDto> comments;

    private Long categoryId;
}
