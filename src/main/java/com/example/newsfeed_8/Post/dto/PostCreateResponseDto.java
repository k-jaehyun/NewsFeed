package com.example.newsfeed_8.Post.dto;

import lombok.Getter;

@Getter
public class PostCreateResponseDto {
    private Long postId;

    public PostCreateResponseDto(Long postId) {
        this.postId=postId;
    }
}
