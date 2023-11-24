package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.PostResponseDto;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/newsfeed")
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
    @GetMapping("/myposts")  // api수정 필요 혹은 PostController에 있는 것으로 대체
    public List<PostResponseDto> getOwnPostList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            return postService.getOwnPostList(memberDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}

