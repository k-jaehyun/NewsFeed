package com.example.newsfeed_8.Post.controller;

import com.example.newsfeed_8.Post.dto.PostCreateResponseDto;
import com.example.newsfeed_8.Post.dto.PostRequestDto;
import com.example.newsfeed_8.Post.dto.PostResponseDto;
import com.example.newsfeed_8.Post.repository.PostRepository;
import com.example.newsfeed_8.Post.service.PostService;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping("")
    public String createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletResponse response) {
        if(memberDetails==null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "error";
        } else {
            PostCreateResponseDto createdPost = postService.createPost(requestDto, memberDetails.getMember());
            return "redirect:/api/posts/" + createdPost.getPostId();
        }
    }

    @ResponseBody
    @GetMapping("/{postId}")     //no-auth
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }


    @PatchMapping("/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletResponse response) {
        if(memberDetails==null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "error";
        } else {
            postService.updatePost(postId, requestDto, memberDetails);
            return "redirect:/api/posts/" + postId;
        }
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletResponse response) {
        if (memberDetails == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "error";
        } else {
            postService.deletePost(postId, memberDetails);
            return "redirect:/api/posts";
        }
    }


}