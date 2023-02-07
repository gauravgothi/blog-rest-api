package com.springboot.blogapp.service.impl;

import com.springboot.blogapp.dto.PostDto;
import com.springboot.blogapp.dto.PostResponse;
import com.springboot.blogapp.entity.Category;
import com.springboot.blogapp.entity.Post;
import com.springboot.blogapp.exception.ResourceNotFoundException;
import com.springboot.blogapp.repository.CategoryRepository;
import com.springboot.blogapp.repository.PostRepository;
import com.springboot.blogapp.service.PostService;
import org.modelmapper.ModelMapper;
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

    private ModelMapper mapper;
    private PostRepository postRepository;

    private CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper,CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper=mapper;
        this.categoryRepository=categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));

            Post post = convertPostDtoToPost(postDto);
            post.setCategory(category);
            Post newPost = postRepository.save(post);

            //convert entity to again in PostDTO for giving response
            PostDto postResponce = convertPostToPostDto(newPost);

            return postResponce;

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

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);
        return convertPostToPostDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        //delete post from database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);

    }

    //Get all posts by category id
    @Override
    public List<PostDto> getPostByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                         .orElseThrow(()-> new ResourceNotFoundException("Category","id",categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map((post) -> convertPostToPostDto(post)).collect(Collectors.toList());
    }


    //convert DTO to entity
    private Post convertPostDtoToPost(PostDto postDto)  {
        Post post = mapper.map(postDto,Post.class);

//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }

    //Convert Post to DTO object
    private PostDto convertPostToPostDto(Post post) {
        PostDto postDto = mapper.map(post,PostDto.class);

//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;

    }
}
