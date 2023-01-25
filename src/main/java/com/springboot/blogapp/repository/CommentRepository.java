package com.springboot.blogapp.repository;

import com.springboot.blogapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//No need to add Repository annotation because JpaRepository extends SimpleRepository class which defind it

public interface CommentRepository extends JpaRepository<Comment,Long> {

    public List<Comment> findByPostId(Long postId);
}
