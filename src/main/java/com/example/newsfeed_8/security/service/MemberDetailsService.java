package com.example.newsfeed_8.security.service;

import com.example.newsfeed_8.Member.entity.Member;
import com.example.newsfeed_8.Member.repository.MemberRepository;
import com.example.newsfeed_8.security.entity.MemberDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService {

    @Autowired
    private final MemberRepository memberRepository;

    public UserDetails getMemberDetails(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found" + userId));
        return new MemberDetailsImpl(member);
    }
}
