package com.springboot.blogapp.controller;

import com.springboot.blogapp.dto.PostDto;
import com.springboot.blogapp.dto.PostResponse;
import com.springboot.blogapp.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.springboot.blogapp.util.AppConstant.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;  //argument constructor make object by default so dont need for autowired
    //Interface provide loose coupling

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //Create post of blog rest api
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto)  {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all post
    @GetMapping
    public PostResponse getAllPost(
            @RequestParam(value = "pageNo",defaultValue = DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = DEFAULT_SORTING_FIELD,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = DEFAULT_SORTING_DIRECTION,required = false) String sortDir) {
        return postService.getAllPost(pageNo,pageSize,sortBy,sortDir);
    }

    //get post by post
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(value = "id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    //update post by id rest api
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable(name = "id") long id)  {
        PostDto postResponse = postService.updatePost(postDto,id);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    //delete post by id rest api
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "id") Long id)   {

        postService.deletePost(id);

        return new ResponseEntity<>("Post deleted successfully ",HttpStatus.OK);
    }
}
