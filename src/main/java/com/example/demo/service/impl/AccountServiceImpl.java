package com.example.demo.service.impl;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.AccountService;
import com.example.demo.utility.Constants;
import com.example.demo.utility.EmailConstructor;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountService accountService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Transactional
    public User saveUser(String name, String username, String email) {
        String password = RandomStringUtils.randomAlphanumeric(10);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setPassword(encryptedPassword);
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, accountService.findUserRoleByName("USER")));
        user.setUserRoles(userRoles);
        userRepository.save(user);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
            String fileName = user.getId() + ".png";
            Path path = Paths.get(Constants.USER_FOLDER + fileName);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mailSender.send(emailConstructor.constructNewUserEmail(user, password));
        return user;
    }

    @Override
    public void updateUserPassword(User appUser, String newpassword) {
        String encryptedPassword = bCryptPasswordEncoder.encode(newpassword);
        appUser.setPassword(encryptedPassword);
        userRepository.save(appUser);
        mailSender.send(emailConstructor.constructResetPasswordEmail(appUser, newpassword));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }

    @Override
    public Role findUserRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    public User simpleSaveUser(User user) {
        userRepository.save(user);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(user));
        return user;

    }

    @Override
    public User updateUser(User user, HashMap<String, String> request) {
        String name = request.get("name");
        // String username = request.get("username");
        String email = request.get("email");
        String bio = request.get("bio");
        user.setName(name);
        // appUser.setUsername(username);
        user.setEmail(email);
        user.setBio(bio);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(user));
        return user;

    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void deleteUser(User appUser) {
        userRepository.delete(appUser);

    }

    @Override
    public void resetPassword(User user) {
        String password = RandomStringUtils.randomAlphanumeric(10);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructResetPasswordEmail(user, password));

    }

    @Override
    public List<User> getUsersListByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    @Override
    public String saveUserImage(MultipartFile multipartFile, Long userImageId) {
        /*
         * MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)
         * request; Iterator<String> it = multipartRequest.getFileNames(); MultipartFile
         * multipartFile = multipartRequest.getFile(it.next());
         */
        byte[] bytes;
        try {
            Files.deleteIfExists(Paths.get(Constants.USER_FOLDER + "/" + userImageId + ".png"));
            bytes = multipartFile.getBytes();
            Path path = Paths.get(Constants.USER_FOLDER + userImageId + ".png");
            Files.write(path, bytes);
            return "User picture saved to server";
        } catch (IOException e) {
            return "User picture Saved";
        }
    }
}
