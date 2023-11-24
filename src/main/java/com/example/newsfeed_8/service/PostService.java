package com.example.newsfeed_8.service;

import com.example.newsfeed_8.dto.PostRequestDto;
import com.example.newsfeed_8.dto.PostResponseDto;
import com.example.newsfeed_8.entity.Member;
import com.example.newsfeed_8.entity.Post;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void createPost(PostRequestDto requestDto, Member member) {
        postRepository.save(new Post(requestDto,member));
    }

    public PostResponseDto getPost(Long postId) {
        return new PostResponseDto(findPost(postId));
    }

    private Post findPost(Long postId) {
        return findPostById(postId);
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
    public Long toggleLikePost(Long postId, Boolean booleanLike, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        Post post = findPostById(postId);

        if (member.getUserId().equals(post.getMember().getUserId())) { return null; }

        List<String> memberIdList = post.getMemberIdList();
        for (String s : memberIdList) {
            if (s.equals(member.getUserId()) && booleanLike) {
                return null;
            } else if (s.equals(member.getUserId()) && !booleanLike) {
                memberIdList.remove(member.getUserId()); // 좋아요 취소
                post.minusLikes();
                return Long.valueOf(post.getLikes());
            }
        }
        if (booleanLike) {
            memberIdList.add(member.getUserId());
            post.plusLikes();
            return Long.valueOf(post.getLikes());
        }
        return null;
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
