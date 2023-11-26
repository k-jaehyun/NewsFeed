package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.CommentRequestDto;
import com.example.newsfeed_8.dto.CommentResponsDto;
import com.example.newsfeed_8.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    private String createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletResponse response) {
        if (memberDetails == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "error";
        } else {
            commentService.createComment(postId, requestDto, memberDetails);
            return "redirect:/api/posts/" + postId + "/comments";
        }
    }

    @ResponseBody
    @GetMapping("/comments")  //no-auth
    private List<CommentResponsDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    @PostMapping("/comments/{commentId}")
    private String updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletResponse response) {
        if (memberDetails == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "error";
        } else {
            commentService.updateComment(postId, commentId, requestDto, memberDetails);
            return "redirect:/api/posts/" + postId + "/comments";
        }
    }

    @DeleteMapping("/comments/{commentId}")
    private String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletResponse response) {
        if (memberDetails == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "error";
        } else {
            commentService.deleteComment(postId, commentId, memberDetails);
            return "redirect:/api/posts/" + postId + "/comments";
        }
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<CommonLikeResponseDto> toggleLikeComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletResponse response) {
        if (memberDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonLikeResponseDto("로그인 한 사용자만 접근 할 수 있습니다. 혹은 유효한 토큰인지 확인하십시오.", HttpStatus.UNAUTHORIZED.value(),null));
        } else {
            return commentService.toggleCommentLike(postId, commentId, memberDetails);
        }
    }




}
