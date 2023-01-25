package com.springboot.blogapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {

    private long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;
    @Email(message = "email shoud be in format ____@___.__")
    @NotEmpty(message = "email should not be empty")
    private String email;
    @NotEmpty
    @Size(min = 3,message = "comment should be at least 2 character")
    private String commentBody;
}
