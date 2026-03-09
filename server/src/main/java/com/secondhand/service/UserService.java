package com.secondhand.service;

import com.secondhand.entity.User;
import java.util.List;

public interface UserService {
    User registerUser(User user);
    
    User getUserById(Long id);
    
    User getUserByUsername(String username);
    
    List<User> getAllUsers();
    
    User updateUser(Long id, User user);
    
    void deleteUser(Long id);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
} 