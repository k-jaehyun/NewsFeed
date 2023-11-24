package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.*;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping("")
    public ResponseEntity<CommonResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            postService.createPost(requestDto, memberDetails.getMember());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new CommonResponseDto("게시물 작성 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/{postId}")     //no-auth
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @GetMapping("/newsFeedList")    //no-auth
    public List<PostListResponseDto> getPostList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostListResponseDto::new).toList();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            postService.updatePost(postId, requestDto, memberDetails);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new CommonResponseDto("게시물 수정 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> deletePost(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            postService.deletePost(postId, memberDetails);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new CommonResponseDto("게시물 삭제 성공", HttpStatus.OK.value()));

    }

    @GetMapping("/{postId}/{booleanLike}")
    public ResponseEntity<LikesResponseDto> toggleLikePost(@PathVariable Long postId, @PathVariable Boolean booleanLike, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

            Long likes = postService.toggleLikePost(postId, booleanLike, memberDetails);
            if(likes != null) {
                if (booleanLike) {
                    return ResponseEntity.ok().body(new LikesResponseDto("좋아요 성공", HttpStatus.OK.value(),likes));
                } else {
                    return ResponseEntity.ok().body(new LikesResponseDto("좋아요 취소 성공", HttpStatus.OK.value(),likes));
                }
            }

        return ResponseEntity.ok().body(new LikesResponseDto("잘못된 요청입니다.", HttpStatus.BAD_REQUEST.value(),null));
    }


}