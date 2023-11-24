package com.example.newsfeed_8.dto;

import com.example.newsfeed_8.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponsDto {

    private String userId;
    private String content;
    private Boolean isDeleted;

    public CommentResponsDto(Comment comment) {
        this.userId=comment.getMember().getUserId();
        this.content=comment.getContent();
        this.isDeleted=comment.getIsDeleted();
    }
}
