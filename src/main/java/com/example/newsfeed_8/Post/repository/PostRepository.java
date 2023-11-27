package com.example.newsfeed_8.Post.repository;

import com.example.newsfeed_8.Post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByMemberIdNotOrderByCreatedAtDesc(Long id);

    List<Post> findTop10ByOrderByCreatedAtDesc();

    List<Post> findAllByMemberIdOrderByCreatedAtDesc(Long id);
}
