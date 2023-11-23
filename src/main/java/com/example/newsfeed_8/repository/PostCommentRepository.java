package com.example.newsfeed_8.repository;

import com.example.newsfeed_8.entity.Comment;
import com.example.newsfeed_8.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
