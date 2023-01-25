package com.springboot.blogapp.service.impl;

import com.springboot.blogapp.dto.CommentDto;
import com.springboot.blogapp.entity.Comment;
import com.springboot.blogapp.entity.Post;
import com.springboot.blogapp.exception.BlogApiException;
import com.springboot.blogapp.exception.ResourceNotFoundException;
import com.springboot.blogapp.repository.CommentRepository;
import com.springboot.blogapp.repository.PostRepository;
import com.springboot.blogapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private ModelMapper mapper;
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    //No need of autowired because constructor is defined here
    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = convertCommentDtoToComment(commentDto);

        //Get post by Post id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post" , "id" ,postId));
        //set post to comment entity
        comment.setPost(post);
        //comment entity to DB
        Comment newComment = commentRepository.save(comment);

        return convertCommentToCommentDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert list of comments to commentsDTO
        return comments.stream().map(comment -> convertCommentToCommentDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByPostIdCommentId(Long postId, Long commentId) {
        //Get post by Post id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post" , "id" ,postId));
        //Get comment by commentId
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));
        //check comment belongs to post or not
        if(!comment.getPost().getId().equals(post.getId()))  {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment not belongs to post.");
        }

        return convertCommentToCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        //Get post by Post id
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new ResourceNotFoundException("Post" , "id" ,postId));
        //Get comment by commentId
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));
        //check comment belongs to post or not
        if(!comment.getPost().getId().equals(post.getId()))  {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment not belongs to post.");
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setCommentBody(commentRequest.getCommentBody());

        Comment updatedComment = commentRepository.save(comment);
        return convertCommentToCommentDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        //Get post by Post id
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new ResourceNotFoundException("Post" , "id" ,postId));
        //Get comment by commentId
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));
        //check comment belongs to post or not
        if(!comment.getPost().getId().equals(post.getId()))  {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment not belongs to post.");
        }
        commentRepository.deleteById(commentId);
    }

    //Convert Comment Entity to Comment DTO
    private CommentDto convertCommentToCommentDto(Comment comment)  {
        CommentDto commentDto = mapper.map(comment,CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setCommentBody(comment.getCommentBody());
        return commentDto;
    }

    //Convert CommentDto to Comment
    private Comment convertCommentDtoToComment(CommentDto commentDto)   {

        Comment comment = mapper.map(commentDto,Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setCommentBody(commentDto.getCommentBody());
        return comment;
    }
}
