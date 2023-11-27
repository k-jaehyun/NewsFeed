package com.example.newsfeed_8.Post.service;

import com.example.newsfeed_8.Member.entity.Member;
import com.example.newsfeed_8.Post.entity.Post;
import com.example.newsfeed_8.Post.dto.PostCreateResponseDto;
import com.example.newsfeed_8.Post.dto.PostRequestDto;
import com.example.newsfeed_8.Post.dto.PostResponseDto;
import com.example.newsfeed_8.PostLike.repository.PostLikeRepository;
import com.example.newsfeed_8.Post.repository.PostRepository;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
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
