package com.example.newsfeed_8.CommentLike.service;

import com.example.newsfeed_8.Comment.entity.Comment;
import com.example.newsfeed_8.Comment.repository.CommentRepository;
import com.example.newsfeed_8.CommentLike.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.CommentLike.entity.CommentLike;
import com.example.newsfeed_8.CommentLike.repository.CommentLikeRepositoy;
import com.example.newsfeed_8.Member.entity.Member;
import com.example.newsfeed_8.Post.entity.Post;
import com.example.newsfeed_8.Post.repository.PostRepository;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepositoy commentLikeRepositoy;

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

}