package com.example.newsfeed_8.PostLike.entity;

import com.example.newsfeed_8.Member.entity.Member;
import com.example.newsfeed_8.Post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="post_like")
@Getter
@NoArgsConstructor
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column
    private Boolean isLike = false;

    public PostLike(Post post, Member member, boolean b) {
        this.post=post;
        this.member=member;
        this.isLike=b;
    }

    public void setIsLike(Boolean trueOrFalse) {
        this.isLike = trueOrFalse;
    }
}
