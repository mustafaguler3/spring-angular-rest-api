package com.example.demo.service.impl;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.AccountService;
import com.example.demo.utility.EmailConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailConstructor emailConstructor;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void saveUser(User user) {
        String password = RandomStringUtils.randomAlphabetic(10);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructNewUserEmail(user,password));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }

    @Override
    public Role findUserRoleByName(String role) {
        return roleRepository.findRoleByName(role);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void updateUser(User user) {
        String password = user.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(user));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void resetPassword(User user) {
        String password = RandomStringUtils.randomAlphabetic(10);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructResetPasswordEmail(user,password));
    }

    @Override
    public List<User> getUserListByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    @Override
    public User simpleSave(User user) {
        userRepository.save(user);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(user));

        return user;
    }
}
