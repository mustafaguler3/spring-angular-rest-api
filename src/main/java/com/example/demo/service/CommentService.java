package com.example.demo.service;

import com.example.demo.models.Comment;
import com.example.demo.models.Post;

public interface CommentService {

    void saveComment(Post post, String username, String content);

}
