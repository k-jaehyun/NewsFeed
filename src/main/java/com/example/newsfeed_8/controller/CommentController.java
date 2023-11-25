package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.CommentRequestDto;
import com.example.newsfeed_8.dto.CommentResponsDto;
import com.example.newsfeed_8.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.CommentService;
import lombok.RequiredArgsConstructor;
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
    private String createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
            commentService.createComment(postId, requestDto, memberDetails);
        return "redirect:/api/posts/"+postId+"/comments";
    }

    @ResponseBody
    @GetMapping("/comments")  //no-auth
    private List<CommentResponsDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    @PostMapping("/comments/{commentId}")
    private String updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        commentService.updateComment(postId, commentId, requestDto, memberDetails);
        return "redirect:/api/posts/"+postId+"/comments";
    }

    @DeleteMapping("/comments/{commentId}")
    private String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        commentService.deleteComment(postId, commentId, memberDetails);
        return "redirect:/api/posts/"+postId+"/comments";
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<CommonLikeResponseDto> toggleLikeComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return commentService.toggleCommentLike(postId,commentId,memberDetails);
    }




}
