package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.*;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping("")
    public String createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        PostCreateResponseDto createdPost = postService.createPost(requestDto, memberDetails.getMember());
        return "redirect:/api/posts/"+createdPost.getPostId();
    }

    @ResponseBody
    @GetMapping("/{postId}")     //no-auth
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @ResponseBody
    @GetMapping("")    //no-auth
    public List<PostListResponseDto> getPostList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostListResponseDto::new).toList();
    }

    @PatchMapping("/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
            postService.updatePost(postId, requestDto, memberDetails);
        return "redirect:/api/posts/"+postId;
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
            postService.deletePost(postId, memberDetails);
            return "redirect:/api/posts";
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<CommonLikeResponseDto> toggleLikePost(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return postService.togglePostLike(postId,memberDetails);
    }

}