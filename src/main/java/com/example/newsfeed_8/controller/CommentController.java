package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.CommentRequestDto;
import com.example.newsfeed_8.dto.CommentResponsDto;
import com.example.newsfeed_8.dto.CommonResponseDto;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    private RedirectView createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        RedirectView redirectView = new RedirectView("redirect:/api/comments/"+postId);

        try {
            commentService.createComment(postId, requestDto, memberDetails);
        } catch (IllegalArgumentException e) {
            redirectView.setStatusCode(HttpStatus.BAD_REQUEST);
            return redirectView;
        }

        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        return redirectView;
    }

    @ResponseBody
    @GetMapping("/{postId}")  //no-auth
    private List<CommentResponsDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }





}
