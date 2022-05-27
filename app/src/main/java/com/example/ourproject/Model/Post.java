package com.example.ourproject.Model;

import com.google.firebase.database.Exclude;

public class Post {
    private String id,post,imageUrl,imagePost,username;


    private String key;

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public Post() {
    }

    public Post(String id, String post, String imageUrl,String imagePost,String username) {
        this.id = id;
        this.post = post;
        this.imageUrl = imageUrl;
        this.imagePost=imagePost;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePost() {
        return imagePost;
    }

    public void setImagePost(String imagePost) {
        this.imagePost = imagePost;
    }
}
