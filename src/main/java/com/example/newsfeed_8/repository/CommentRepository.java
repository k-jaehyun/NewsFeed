package com.example.newsfeed_8.repository;

import com.example.newsfeed_8.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
