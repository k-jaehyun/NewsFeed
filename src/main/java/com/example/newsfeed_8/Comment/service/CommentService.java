package com.example.newsfeed_8.Comment.service;

import com.example.newsfeed_8.Comment.dto.CommentRequestDto;
import com.example.newsfeed_8.Comment.dto.CommentResponsDto;
import com.example.newsfeed_8.Comment.entity.Comment;
import com.example.newsfeed_8.Comment.repository.CommentRepository;
import com.example.newsfeed_8.Member.entity.Member;
import com.example.newsfeed_8.Post.entity.Post;
import com.example.newsfeed_8.CommentLike.repository.CommentLikeRepositoy;
import com.example.newsfeed_8.Post.repository.PostRepository;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
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