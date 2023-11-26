package com.example.newsfeed_8.repository;

import com.example.newsfeed_8.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByMemberIdNotOrderByCreatedAtDesc(Long id);

    List<Post> findTop10ByOrderByCreatedAtDesc();

    List<Post> findAllByMemberIdOrderByCreatedAtDesc(Long id);
}
