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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private Boolean isDeleted= false;

    @OneToMany(mappedBy = "comment")
    private List<PostComment> postCommentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Comment(CommentRequestDto requestDto, Member member) {
        this.content= requestDto.getContent();
        this.member=member;
    }
}
