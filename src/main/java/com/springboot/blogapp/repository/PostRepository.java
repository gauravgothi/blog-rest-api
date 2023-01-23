package com.springboot.blogapp.repository;

import com.springboot.blogapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    public List<Post> findByTitle(String title);

}
