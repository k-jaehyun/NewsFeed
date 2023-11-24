package com.example.newsfeed_8.entity;

import com.example.newsfeed_8.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String img;

    private List<String> memberIdList = new ArrayList<>();

    @Column
    private Long likes = 0L;

    @OneToMany(mappedBy = "post")
    private List<PostComment> postCommentList = new ArrayList<>();

    public Post(PostRequestDto requestDto, Member member) {
        this.member = member;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.img = requestDto.getImg();
    }

    public void update(PostRequestDto requestDto) {
        this.title=requestDto.getTitle();
        this.content= requestDto.getContent();
        this.img= requestDto.getImg();
    }

    public void plusLikes() {
        this.likes++;
    }

    public void minusLikes() {
        this.likes--;
    }
}
