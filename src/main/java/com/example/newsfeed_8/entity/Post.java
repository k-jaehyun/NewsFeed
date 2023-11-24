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

    @OneToMany(mappedBy = "post")  //LAZY 주의
    private List<Comment> commentList = new ArrayList<>();

//    @OneToMany(mappedBy = "post")  //LAZY 주의
//    private List<Like> likeList = new ArrayList<>();

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

}
