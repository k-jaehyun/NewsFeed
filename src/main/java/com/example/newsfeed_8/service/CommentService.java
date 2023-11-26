package com.example.newsfeed_8.service;

import com.example.newsfeed_8.dto.CommentRequestDto;
import com.example.newsfeed_8.dto.CommentResponsDto;
import com.example.newsfeed_8.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.entity.*;
import com.example.newsfeed_8.repository.CommentLikeRepositoy;
import com.example.newsfeed_8.repository.CommentRepository;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepositoy commentLikeRepositoy;

    public void createComment(Long postId, CommentRequestDto requestDto, MemberDetailsImpl memberDetails) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Comment comment = new Comment(requestDto, memberDetails.getMember(),post);
        commentRepository.save(comment);
    }

    public List<CommentResponsDto> getComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return commentRepository.findAllByPostId(post.getId()).stream().map(CommentResponsDto::new).toList();
    }

    @Transactional
    public void updateComment(Long postId, Long commentId, CommentRequestDto requestDto, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        Comment comment = validatePostCommentMember(postId,commentId,member);
        comment.update(requestDto);
    }

    public void deleteComment(Long postId, Long commentId, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        Comment comment = validatePostCommentMember(postId,commentId,member);
        commentRepository.delete(comment);
    }

    @Transactional
    public ResponseEntity<CommonLikeResponseDto> toggleCommentLike(Long postId, Long commentId, MemberDetailsImpl memberDetails) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
            Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
            Member member = memberDetails.getMember();

            if (comment.getMember().getUserId().equals(member.getUserId())) {
                throw new IllegalArgumentException("본인의 게시글 입니다.");
            }

            CommentLike commentLike = commentLikeRepositoy.findByCommentIdAndMemberId(comment.getId(), member.getId());

            if (commentLike == null) {
                commentLikeRepositoy.save(new CommentLike(comment,member,true));
            } else {
                commentLike.setIsLike(!commentLike.getIsLike());
            }

            Long likes = (long) commentLikeRepositoy.countByCommentIdAndIsLikeTrue(comment.getId());

            return ResponseEntity.ok().body(new CommonLikeResponseDto("좋아요/좋아요취소 성공", HttpStatus.OK.value(),likes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonLikeResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(),null));
        }
    }

    private Comment validatePostCommentMember(Long postId, Long commentId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException("게시글 내 해당 댓글이 존재하지 않습니다.");
        }
        if(!member.getId().equals(comment.getMember().getId())) {
            throw  new IllegalArgumentException("해당 개시글의 작성자가 아닙니다.");
        }

        return comment;
    }

}