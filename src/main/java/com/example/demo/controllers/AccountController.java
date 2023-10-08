package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.service.AccountService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class AccountController {

    private Long userImageId;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;

    @GetMapping("/list")
    public ResponseEntity<?> getUserList(){
        List<User> users = accountService.userList();
        if (users.isEmpty()){
            return new ResponseEntity<>("No users found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable("username") String username){
        User user = accountService.findByUsername(username);
        if (user == null){
            return new ResponseEntity<>("No user found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("findByUsername/{username}")
    public ResponseEntity<?> getUserListByUsername(@PathVariable("username") String username){
        List<User> user = accountService.getUsersListByUsername(username);
        if (user.isEmpty()){
            return new ResponseEntity<>("No user found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody HashMap<String,String> request){
        String username = request.get("username");
        if (accountService.findByUsername(username) != null){
            return new ResponseEntity<>("usernameExist",HttpStatus.CONFLICT);
        }
        String email = request.get("email");
        if (accountService.findByEmail(email) != null){
            return new ResponseEntity<>("emailExist",HttpStatus.CONFLICT);
        }
        String name = request.get("name");
        try {
            User user = accountService.saveUser(name,username,email);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("An error occured",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody HashMap<String,String> request){
        String id = request.get("id");
        User user= accountService.findUserById(Long.parseLong(id));
        if (user == null){
            return new ResponseEntity<>("userNotFound",HttpStatus.NOT_FOUND);
        }
        try {
            accountService.updateUser(user,request);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("An error occured",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/photo/upload")
    public ResponseEntity<String> fileUpload(@RequestParam("image") MultipartFile multipartFile) {
        try {
            accountService.saveUserImage(multipartFile, userImageId);
            return new ResponseEntity<>("User Picture Saved!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User Picture Not Saved", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody HashMap<String,String> request){
        String username = request.get("username");
        User user = accountService.findByUsername(username);
        if (user == null){
            return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
        }

        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");
        if (!newPassword.equals(confirmPassword)){
            return new ResponseEntity<>("PasswordNotMatched",HttpStatus.BAD_REQUEST);
        }
        String userPassword = user.getPassword();

        try {
            if (newPassword != null && !newPassword.isEmpty() && !StringUtils.isEmpty(newPassword)){
                if (passwordEncoder.matches(currentPassword,userPassword)){
                    accountService.updateUserPassword(user,newPassword);
                }else {
                    return new ResponseEntity<>("IccorrectCurrentPassword", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Password changed successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable("email") String email){
        User user = accountService.findByEmail(email);
        if (user == null){
            return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
        }
        accountService.resetPassword(user);
        return new ResponseEntity<>("Email sent",HttpStatus.OK);
    }
}




















