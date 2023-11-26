package com.example.newsfeed_8.dto;

import com.example.newsfeed_8.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponsDto {

    private Long commentId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponsDto(Comment comment) {
        this.commentId=comment.getId();
        this.userId=comment.getMember().getUserId();
        this.content=comment.getContent();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt=comment.getModifiedAt();
    }
}
