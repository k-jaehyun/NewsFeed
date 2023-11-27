package com.example.newsfeed_8.Member.entity;

import com.example.newsfeed_8.Comment.entity.Comment;
import com.example.newsfeed_8.Post.entity.Post;
import com.example.newsfeed_8.PostLike.entity.PostLike;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Setter
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String introduction;

    @OneToMany(mappedBy = "member")  //LAZY 주의
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member")  //LAZY 주의
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member")  //LAZY 주의
    private List<PostLike> postLikeList = new ArrayList<>();

    public Member(String userId, String password, String email, String introduction) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.introduction = introduction;
    }
}
