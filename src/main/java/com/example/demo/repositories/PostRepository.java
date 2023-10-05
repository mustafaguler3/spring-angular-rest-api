package com.example.demo.repositories;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("select p from Post p order by p.postedDate DESC")
    List<Post> findAll();

    @Query("select p from Post p where p.username=:username order by p.postedDate DESC")
    List<Post> findByUsername(@Param("username") String username);

    @Query("select p from Post p where p.id=:x")
    Post findPostById(@Param("x") Long id);

    @Modifying
    @Query("delete Post where id=:x")
    void deletePostById(@Param("x") Long id);
}




















