package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.CommonResponseDto;
import com.example.newsfeed_8.dto.PostListResponseDto;
import com.example.newsfeed_8.dto.PostReqeustDto;
import com.example.newsfeed_8.dto.PostResponseDto;
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
@RequestMapping("/api")
public class PostController {
    private PostService postService;
    private PostRepository postRepository;

    @PostMapping("/post")
    public ResponseEntity<CommonResponseDto> createPost(@RequestBody PostReqeustDto reqeustDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        try {
            postService.createPost(reqeustDto,memberDetails.getMember());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new CommonResponseDto("포스팅 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/post/{post_id}")     //no-auth
    public PostResponseDto getPost(@PathVariable Long post_id) {
        return postService.getPost(post_id);
    }

    @GetMapping("/post/news_feed_list")    //no-auth
    public List<PostListResponseDto> getPostList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostListResponseDto::new).toList();
    }

    @PatchMapping("/post/{post_id}")
    public String updatePost(@PathVariable Long post_id, @RequestBody PostReqeustDto reqeustDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return postService.updatePost(post_id,reqeustDto,memberDetails);
    }

    @DeleteMapping("/post/{post_id}")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return postService.deletePost(id,memberDetails);
    }


}
