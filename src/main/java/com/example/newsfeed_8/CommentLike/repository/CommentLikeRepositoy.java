package com.example.newsfeed_8.CommentLike.repository;

import com.example.newsfeed_8.CommentLike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepositoy extends JpaRepository<CommentLike, Long> {
    CommentLike findByCommentIdAndMemberId(Long commentId, Long memberId);

    Object countByCommentIdAndIsLikeTrue(Long commentId);
}
