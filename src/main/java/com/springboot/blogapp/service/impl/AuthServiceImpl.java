package com.springboot.blogapp.service.impl;

import com.springboot.blogapp.dto.LoginDto;
import com.springboot.blogapp.dto.RegisterDto;
import com.springboot.blogapp.entity.Role;
import com.springboot.blogapp.entity.User;
import com.springboot.blogapp.exception.BlogApiException;
import com.springboot.blogapp.repository.RoleRepository;
import com.springboot.blogapp.repository.UserRepository;
import com.springboot.blogapp.security.JwtTokenProvider;
import com.springboot.blogapp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder,JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider=jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {
        //Validation
        //check for username already exist in DB
        if(userRepository.existsByUsername(registerDto.getUsername()))  {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"username is already exist!...");
        }
        //check for email already exist in DB
        if(userRepository.existsByEmail(registerDto.getEmail()))  {
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"email is already exist!...");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        return "User Registered Successfully!......";
    }
}
