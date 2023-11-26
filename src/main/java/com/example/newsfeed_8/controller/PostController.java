package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.*;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
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

        // 헤더에  url이 담기는 코드
//        try {
//            PostCreateResponseDto createdPost = postService.createPost(requestDto, memberDetails.getMember());
//
//            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                    .path("/api/posts/{postId}")
//                    .buildAndExpand(createdPost.getPostId())
//                    .toUri();
//
//            return ResponseEntity.created(location)
//                    .body(new CommonResponseDto("게시물 게시 성공", HttpStatus.CREATED.value()));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest()
//                    .body(new CommonResponseDto("뭔가 잘못되었습니다.", HttpStatus.BAD_REQUEST.value()));
//        }

    }

    @ResponseBody
    @GetMapping("/{postId}")     //no-auth
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
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