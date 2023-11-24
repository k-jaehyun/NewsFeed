package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.CommentRequestDto;
import com.example.newsfeed_8.dto.CommentResponsDto;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    private String createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
            commentService.createComment(postId, requestDto, memberDetails);
        return "redirect:/api/comments/"+postId;
    }

    @ResponseBody
    @GetMapping("/{postId}")  //no-auth
    private List<CommentResponsDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }





}
