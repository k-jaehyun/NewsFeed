package com.example.newsfeed_8.Comment.controller;

import com.example.newsfeed_8.Comment.dto.CommentRequestDto;
import com.example.newsfeed_8.Comment.dto.CommentResponsDto;
import com.example.newsfeed_8.Comment.service.CommentService;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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


}
