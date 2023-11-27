package com.example.newsfeed_8.PostLike.service;

import com.example.newsfeed_8.CommentLike.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.Member.entity.Member;
import com.example.newsfeed_8.Post.entity.Post;
import com.example.newsfeed_8.Post.repository.PostRepository;
import com.example.newsfeed_8.PostLike.entity.PostLike;
import com.example.newsfeed_8.PostLike.repository.PostLikeRepository;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public ResponseEntity<CommonLikeResponseDto> togglePostLike(Long postId, MemberDetailsImpl memberDetails) {
        try {
            Post post = findPostById(postId);
            Member member = memberDetails.getMember();

            if (post.getMember().getUserId().equals(member.getUserId())) {
                throw new IllegalArgumentException("본인의 게시글 입니다.");
            }

            PostLike postLike = postLikeRepository.findByPostIdAndMemberId(post.getId(), member.getId());

            if (postLike == null) {
                postLikeRepository.save(new PostLike(post,member,true));
            } else {
                postLike.setIsLike(!postLike.getIsLike());
            }

            Long likes = (long) postLikeRepository.countByPostIdAndIsLikeTrue(post.getId());

            return ResponseEntity.ok().body(new CommonLikeResponseDto("좋아요/좋아요취소 성공", HttpStatus.OK.value(),likes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonLikeResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(),null));
        }

    }

    private Post findPostById (Long id) {
        return postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 postId입니다."));
    }
}