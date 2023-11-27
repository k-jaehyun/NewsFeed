package com.example.newsfeed_8.Post.controller;

import com.example.newsfeed_8.Post.dto.PostResponseDto;
import com.example.newsfeed_8.Post.entity.Post;
import com.example.newsfeed_8.Post.repository.PostRepository;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class NewsFeedController {

    private final PostRepository postRepository;

    public NewsFeedController( PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    // 로그인 한 사용자 게시물을 제외한 모든 게시물 조회
    // 토큰을 빼면 전체 조회 가능 (신기능 )
    @GetMapping("")
    public List<PostResponseDto> getOtherPostList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            if (memberDetails == null) {
                List<Post> allPosts = postRepository.findAllByOrderByCreatedAtDesc();
                return convertToDtoList(allPosts);
            } else {
                List<Post> otherPosts = postRepository.findAllByMemberIdNotOrderByCreatedAtDesc(memberDetails.getMember().getId());
                return convertToDtoList(otherPosts);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    // 로그인 한 사용자 게시물만 조회
    // 토큰을 빼면 전체 조회 가능 (신기능 )
    @GetMapping("/myposts")
    public List<PostResponseDto> getOwnPostList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            List<Post> ownPosts;
            if (memberDetails == null) {
                ownPosts = postRepository.findTop10ByOrderByCreatedAtDesc(); // Example: get the top 10 posts
            } else {
                ownPosts = postRepository.findAllByMemberIdOrderByCreatedAtDesc(memberDetails.getMember().getId());
            }
            return convertToDtoList(ownPosts);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<PostResponseDto> convertToDtoList(List<Post> posts) {
        return posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

}
