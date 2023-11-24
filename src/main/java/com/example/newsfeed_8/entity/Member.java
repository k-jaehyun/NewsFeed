package com.example.newsfeed_8.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member")
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

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    public Member(String userId, String password, String email, String introduction) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.introduction = introduction;
    }

}
