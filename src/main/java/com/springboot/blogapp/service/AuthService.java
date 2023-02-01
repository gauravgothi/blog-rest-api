package com.springboot.blogapp.service;

import com.springboot.blogapp.dto.LoginDto;
import com.springboot.blogapp.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
