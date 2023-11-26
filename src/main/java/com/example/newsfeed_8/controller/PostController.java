package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.dto.PostCreateResponseDto;
import com.example.newsfeed_8.dto.PostRequestDto;
import com.example.newsfeed_8.dto.PostResponseDto;
import com.example.newsfeed_8.jwt.JwtUtil;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

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

    @PostMapping("/{postId}/like")
    public ResponseEntity<CommonLikeResponseDto> toggleLikePost(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        if (memberDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonLikeResponseDto("로그인 한 사용자만 접근 할 수 있습니다. 혹은 유효한 토큰인지 확인하십시오.", HttpStatus.UNAUTHORIZED.value(),null));
        } else {
            return postService.togglePostLike(postId, memberDetails);
        }
    }

}