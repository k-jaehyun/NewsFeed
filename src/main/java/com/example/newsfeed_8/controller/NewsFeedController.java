package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.PostListResponseDto;
import com.example.newsfeed_8.dto.PostRequestDto;
import com.example.newsfeed_8.dto.PostResponseDto;
import com.example.newsfeed_8.entity.Post;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class NewsFeedController {
    private final PostService postService;

    public NewsFeedController(PostService postService) {
        this.postService = postService;
    }


    // 로그인 한 사용자 게시물을 제외한 모든 게시물조회
    @GetMapping("/otherposts")
    public List<PostResponseDto> getOtherPostList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            return postService.getOtherPostList(memberDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    // 로그인 한 사용자 게시물만 조회
    @GetMapping("/myposts")
    public List<PostResponseDto> getOwnPostList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            return postService.getOwnPostList(memberDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}

