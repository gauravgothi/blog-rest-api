package com.springboot.blogapp.service.impl;

import com.springboot.blogapp.dto.PostDto;
import com.springboot.blogapp.dto.PostResponse;
import com.springboot.blogapp.entity.Post;
import com.springboot.blogapp.exception.ResourceNotFoundException;
import com.springboot.blogapp.repository.PostRepository;
import com.springboot.blogapp.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        //check for if title already exist otherwise on object creation id is incremented
        List<Post> posts = postRepository.findByTitle(postDto.getTitle());
        if(posts.isEmpty()) {

            Post post = convertPostDtoToPost(postDto);
            Post newPost = postRepository.save(post);

            //convert entity to again in PostDTO for giving response
            PostDto postResponce = convertPostToPostDto(newPost);

            return postResponce;
        }
        postDto.setTitle("Your Title is duplicate");
        return postDto;
    }

    @Override
    public PostResponse getAllPost(int pageNo,int pageSize,String sortBy,String sortDir) {
        //sorting logic
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        //create pageRequest instance
        PageRequest pageRequest = PageRequest.of(pageNo,pageSize,sort);
        Page<Post> posts = postRepository.findAll(pageRequest);
        //get content of page object in list
        List<Post> postList = posts.getContent();

        //Lambda Expression uses stream object
        List<PostDto> content =  postList.stream().map(i -> convertPostToPostDto(i)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        return convertPostToPostDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        //get post by id from database if id not exist we will throw exception
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return convertPostToPostDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        //delete post from database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);

    }


    //convert DTO to entity
    private Post convertPostDtoToPost(PostDto postDto)  {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }

    //Convert Post to DTO object
    private PostDto convertPostToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;

    }
}
