package com.example.newsfeed_8.Comment.entity;

import com.example.newsfeed_8.Comment.dto.CommentRequestDto;
import com.example.newsfeed_8.Post.entity.Post;
import com.example.newsfeed_8.CommentLike.entity.CommentLike;
import com.example.newsfeed_8.Member.entity.Member;
import com.example.newsfeed_8.Timestamped.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="comment")
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "comment",orphanRemoval = true)  //LAZY 주의
    private List<CommentLike> commentLikeList = new ArrayList<>();

    public Comment(CommentRequestDto requestDto, Member member, Post post) {
        this.content= requestDto.getContent();
        this.member=member;
        this.post=post;
    }

    public void update(CommentRequestDto requestDto) {
        this.content=requestDto.getContent();
    }
}
