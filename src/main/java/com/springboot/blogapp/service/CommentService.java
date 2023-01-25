package com.springboot.blogapp.service;

import com.springboot.blogapp.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long postId,CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(Long postId);

    CommentDto getCommentByPostIdCommentId(Long postId,Long commentId);

    CommentDto updateComment(Long postId,Long commentId,CommentDto commentRequest);

    void deleteComment(Long postId,Long commentId);
}
