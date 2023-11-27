package com.example.newsfeed_8.CommentLike.entity;

import com.example.newsfeed_8.Comment.entity.Comment;
import com.example.newsfeed_8.Member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="comment_like")
@Getter
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Column
    private Boolean isLike = false;

    public CommentLike(Comment comment, Member member, boolean b) {
        this.comment=comment;
        this.member=member;
        this.isLike=b;
    }

    public void setIsLike(Boolean trueOrFalse) {
        this.isLike = trueOrFalse;
    }
}
