package com.example.newsfeed_8.CommentLike.controller;

import com.example.newsfeed_8.CommentLike.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.CommentLike.service.CommentLikeService;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/api/posts/{postId}")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;


    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<CommonLikeResponseDto> toggleLikeComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletResponse response) {
        if (memberDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonLikeResponseDto("로그인 한 사용자만 접근 할 수 있습니다. 혹은 유효한 토큰인지 확인하십시오.", HttpStatus.UNAUTHORIZED.value(),null));
        } else {
            return commentLikeService.toggleCommentLike(postId, commentId, memberDetails);
        }
    }
}