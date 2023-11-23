package com.example.newsfeed_8.repository;

import com.example.newsfeed_8.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
