package com.example.newsfeed_8.PostLike.controller;

import com.example.newsfeed_8.CommentLike.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.PostLike.service.PostLikeService;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<CommonLikeResponseDto> toggleLikePost(@PathVariable Long postId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        if (memberDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonLikeResponseDto("로그인 한 사용자만 접근 할 수 있습니다. 혹은 유효한 토큰인지 확인하십시오.", HttpStatus.UNAUTHORIZED.value(),null));
        } else {
            return postLikeService.togglePostLike(postId, memberDetails);
        }
    }
}