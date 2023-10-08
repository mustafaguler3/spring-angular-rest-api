package com.example.demo.service;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface PostService {

    Post savePost(User user, HashMap<String,String> request,String postImageName);
    List<Post> postList();
    Post getPostById(Long id);
    List<Post> findPostByUsername(String username);
    Post deletePost(Post post);
    String savePostImage(MultipartFile multipartFile, String fileName);
}




















