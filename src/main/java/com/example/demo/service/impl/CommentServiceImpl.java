package com.example.demo.service.impl;

import com.example.demo.models.Comment;
import com.example.demo.models.Post;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void saveComment(Post post, String username, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUsername(username);
        comment.setPostedDate(new Date());
        post.setComments(comment);
        commentRepository.save(comment);
    }
}
