package com.example.newsfeed_8.service;

import com.example.newsfeed_8.dto.PostReqeustDto;
import com.example.newsfeed_8.dto.PostResponseDto;
import com.example.newsfeed_8.entity.Member;
import com.example.newsfeed_8.entity.Post;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void createPost(PostReqeustDto reqeustDto, Member member) {
        postRepository.save(new Post(reqeustDto,member));
    }

    public PostResponseDto getPost(Long postId) {
        return new PostResponseDto(findPost(postId));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 포스트 입니다."));
    }

    @Transactional
    public String updatePost(Long postId, PostReqeustDto reqeustDto, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();

        Post post = verifyMember(member,postId);

        post.update(reqeustDto);

        return "수정 성공";
    }

    public String deletePost(Long id, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        Post post = verifyMember(member,id);
        postRepository.delete(post);
        return "삭제 성공";
    }

    private Post verifyMember(Member member, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 postId입니다."));
        if (!post.getMember().getUserId().equals(member.getUserId())) {
            throw new IllegalArgumentException("해당 사용자가 아닙니다.");
        }
        return post;
    }


}
