package com.example.newsfeed_8.service;

import com.example.newsfeed_8.dto.CommonLikeResponseDto;
import com.example.newsfeed_8.dto.PostRequestDto;
import com.example.newsfeed_8.dto.PostResponseDto;
import com.example.newsfeed_8.entity.Like;
import com.example.newsfeed_8.entity.Member;
import com.example.newsfeed_8.entity.Post;
import com.example.newsfeed_8.repository.LikeRepository;
import com.example.newsfeed_8.repository.PostRepository;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

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
    public ResponseEntity<CommonLikeResponseDto> toggleLikePost(Long postId, MemberDetailsImpl memberDetails) {
        try {
            Post post = findPostById(postId);
            Member member = memberDetails.getMember();

            if (post.getMember().getUserId().equals(member.getUserId())) {
                throw new IllegalArgumentException("본인의 게시글 입니다.");
            }

            Like like = likeRepository.findByPostIdAndMemberId(post.getId(), member.getId());

            if (like == null) {
                likeRepository.save(new Like(post,member,true));
            } else {
                like.setIsLike(!like.getIsLike());
            }

            Long likes = (long) likeRepository.countByPostIdAndIsLikeTrue(post.getId());

            return ResponseEntity.ok().body(new CommonLikeResponseDto("좋아요/좋아요취소 성공", HttpStatus.OK.value(),likes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonLikeResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(),null));
        }

    }

    public List<PostResponseDto> getMyPostList(MemberDetailsImpl memberDetails) {
        Long memberId = memberDetails.getMember().getId();
        return postRepository.findByMemberId(memberId).stream().map(PostResponseDto::new).toList();
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

    // 뉴스 피드
    // 현재 로그인한 사용자가 자신의 글을 제외한 다른 사용자의 모든 게시글 조회
    public List<PostResponseDto> getOtherPostList(MemberDetailsImpl memberDetails) {
        Member currentMember = memberDetails.getMember();


        List<Post> otherUserPosts = postRepository.findByMemberIdNot(currentMember.getId());

        List<PostResponseDto> otherUserPostsDto = otherUserPosts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
        return otherUserPostsDto;
    }
    // 현재 로그인한 사용자 게시물만 조회
    public List<PostResponseDto> getOwnPostList(MemberDetailsImpl memberDetails) {
        Member currentMember = memberDetails.getMember();

        List<Post> ownPosts = postRepository.findByMemberId(currentMember.getId());

        List<PostResponseDto> ownPostsDto = ownPosts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
        return ownPostsDto;

    }

}
