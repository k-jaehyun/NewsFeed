package com.example.newsfeed_8.repository;

import com.example.newsfeed_8.entity.Comment;
import com.example.newsfeed_8.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostCommentList_PostId(Long id);
}
