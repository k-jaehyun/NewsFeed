package com.example.newsfeed_8.Post.dto;

import com.example.newsfeed_8.Post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String user_id;
    private String content;
    private String img;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();;
        this.user_id = post.getMember().getUserId();
        this.content = post.getContent();
        this.img = post.getImg();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
