package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false,nullable = false)
    private Long id;
    private String name;

    private String username;
    @Column(columnDefinition = "text")
    private String caption;
    private String location;
    private int likes;

    @CreationTimestamp
    private Date postedDate;

    private Integer userImageId;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    //@JoinColumn(name = "post_id")
    private List<Comment> commentList;

    public Post() {
    }

    public Post(Long id, String name, String caption, String location, int likes, Date postedDate, Integer userImageId, List<Comment> commentList) {
        this.id = id;
        this.name = name;
        this.caption = caption;
        this.location = location;
        this.likes = likes;
        this.postedDate = postedDate;
        this.userImageId = userImageId;
        this.commentList = commentList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getUserImageId() {
        return userImageId;
    }

    public void setUserImageId(Integer userImageId) {
        this.userImageId = userImageId;
    }


    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Stream<Comment> getCommentList() {
        if (commentList != null) {
            return commentList.stream().sorted(Comparator.comparing(Comment::getPostedDate));
        }
        return null;
    }

    @JsonIgnore
    public void setComments(Comment comment) {
        if (comment != null) {
            this.commentList.add(comment);
        }
    }
}


















