package com.example.newsfeed_8.dto;

import com.example.newsfeed_8.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private String title;
    private String img;
    private LocalDateTime createdAt;

    public PostListResponseDto(Post post) {
        this.title = post.getTitle();
        this.img = post.getImg();
        this.createdAt = post.getCreatedAt();
    }

}
