package com.example.newsfeed_8.repository;

import com.example.newsfeed_8.dto.PostRequestDto;
import com.example.newsfeed_8.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByMemberIdNot(Long currentMemberId);

    List<Post> findByMemberId(Long id);
}
