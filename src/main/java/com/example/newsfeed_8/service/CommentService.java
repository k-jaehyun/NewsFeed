package com.example.newsfeed_8.service;

import com.example.newsfeed_8.dto.CommentRequestDto;
import com.example.newsfeed_8.dto.CommentResponsDto;
import com.example.newsfeed_8.dto.CommonResponseDto;
import com.example.newsfeed_8.entity.Comment;
import com.example.newsfeed_8.entity.Post;
import com.example.newsfeed_8.entity.PostComment;
import com.example.newsfeed_8.repository.CommentRepository;
import com.example.newsfeed_8.repository.PostCommentRepository;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    public void createComment(Long postId, CommentRequestDto requestDto, MemberDetailsImpl memberDetails) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Comment comment = new Comment(requestDto, memberDetails.getMember());
        commentRepository.save(comment);
        postCommentRepository.save(new PostComment(post, comment));
    }

    public List<CommentResponsDto> getComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return commentRepository.findAllByPostCommentList_PostId(post.getId()).stream().map(CommentResponsDto::new).toList();
    }
}