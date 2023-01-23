package com.springboot.blogapp.service;

import com.springboot.blogapp.dto.PostDto;
import com.springboot.blogapp.dto.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPost(int pageNo, int pageSize,String sortBy,String sortDir);

    PostDto getPostById(Long id);

    PostDto updatePost(PostDto postDto,long id);

    void deletePost(Long id);
}
