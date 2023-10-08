package com.example.demo.service;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface AccountService {
    User saveUser(String name, String username, String email);
    User findByUsername(String username);
    User findByEmail(String userEmail);
    List<User> userList();
    Role findUserRoleByName(String string);
    Role saveRole(Role role);
    void updateUserPassword(User user, String newpassword);
    User updateUser(User user, HashMap<String, String> request);
    User simpleSaveUser(User user);
    User findUserById(Long id);
    void deleteUser(User appUser);
    void resetPassword(User user);
    List<User> getUsersListByUsername(String username);
    String saveUserImage(MultipartFile multipartFile, Long userImageId);
}

















