package com.example.newsfeed_8.entity;

import com.example.newsfeed_8.dto.CommentRequestDto;
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

    public Comment(CommentRequestDto requestDto, Member member,Post post) {
        this.content= requestDto.getContent();
        this.member=member;
        this.post=post;
    }

    public void update(CommentRequestDto requestDto) {
        this.content=requestDto.getContent();
    }
}
