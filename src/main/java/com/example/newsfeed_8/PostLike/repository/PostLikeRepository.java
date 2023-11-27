package com.example.newsfeed_8.PostLike.repository;

import com.example.newsfeed_8.PostLike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    PostLike findByPostIdAndMemberId(Long postId, Long memberId);
    Object countByPostIdAndIsLikeTrue(Long postId);
}
