package com.example.newsfeed_8.repository;

import com.example.newsfeed_8.entity.Member;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsService {
    private MemberRepository memberRepository;

    public UserDetails getMemberDetails(String userId){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found" + userId));
        return new MemberDetailsImpl(member);
    }
}
