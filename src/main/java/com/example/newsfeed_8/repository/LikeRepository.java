package com.example.newsfeed_8.repository;

import com.example.newsfeed_8.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findByPostIdAndMemberId(Long id, Long id1);
    Object countByPostIdAndIsLikeTrue(Long id);
}
