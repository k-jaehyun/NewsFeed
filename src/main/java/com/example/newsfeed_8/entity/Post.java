package com.example.newsfeed_8.entity;

import com.example.newsfeed_8.dto.PostReqeustDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="post")
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

    public Post(PostReqeustDto reqeustDto, Member member) {
        this.member = member;
        this.title = reqeustDto.getTitle();
        this.content = reqeustDto.getContent();
        this.img = reqeustDto.getImg();
    }

    public void update(PostReqeustDto reqeustDto) {
        this.title=reqeustDto.getTitle();
        this.content= reqeustDto.getContent();
        this.img= reqeustDto.getImg();
    }
}
