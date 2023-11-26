package com.example.newsfeed_8.service;

import com.example.newsfeed_8.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.dto.PostCreateResponseDto;
import com.example.newsfeed_8.dto.PostRequestDto;
import com.example.newsfeed_8.dto.PostResponseDto;
import com.example.newsfeed_8.entity.Member;
import com.example.newsfeed_8.entity.Post;
import com.example.newsfeed_8.entity.PostLike;
import com.example.newsfeed_8.repository.PostLikeRepository;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public PostCreateResponseDto createPost(PostRequestDto requestDto, Member member) {
        Post post = postRepository.save(new Post(requestDto,member));
        return new PostCreateResponseDto(post.getId());
    }

    public PostResponseDto getPost(Long postId) {
        return new PostResponseDto(findPostById(postId));
    }


    @Transactional
    public String updatePost(Long postId, PostRequestDto requestDto, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();

        Post post = verifyMember(member,postId);

        post.update(requestDto);

        return "수정 성공";
    }

    public String deletePost(Long postId, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        Post post = verifyMember(member,postId);
        postRepository.delete(post);
        return "삭제 성공";
    }

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

    private Post verifyMember(Member member, Long postId) {
        Post post = findPostById(postId);
        if (!post.getMember().getUserId().equals(member.getUserId())) {
            throw new IllegalArgumentException("해당 사용자가 아닙니다.");
        }
        return post;
    }

    private Post findPostById (Long id) {
        return postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 postId입니다."));
    }

}
