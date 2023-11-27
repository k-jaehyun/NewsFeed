package com.example.newsfeed_8.Post.dto;

import com.example.newsfeed_8.Post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long postId;
    private String title;
    private String img;
    private LocalDateTime createdAt;

    public PostListResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.img = post.getImg();
        this.createdAt = post.getCreatedAt();
    }
}
