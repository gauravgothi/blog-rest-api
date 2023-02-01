package com.springboot.blogapp.repository;

import com.springboot.blogapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username,String email);
    Optional<User> findByUsername(String username);
}
