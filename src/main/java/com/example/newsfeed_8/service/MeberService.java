package com.example.newsfeed_8.service;

import com.example.newsfeed_8.dto.MeberRequestDto;
import com.example.newsfeed_8.entity.Member;
import com.example.newsfeed_8.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MeberRequestDto meberRequestDto) {
        String userId = meberRequestDto.getUserId();
        String password = passwordEncoder.encode(meberRequestDto.getPassword());
        String email = meberRequestDto.getEmail();
        String introduction = meberRequestDto.getIntroduction();

        if(memberRepository.findByUserId(userId).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 유저 입니다.");
        }
        Member member = new Member(userId, password, email, introduction);
        memberRepository.save(member);
    }

    public void login(MeberRequestDto meberRequestDto) {
        String userId = meberRequestDto.getUserId();
        String password = meberRequestDto.getPassword();

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new UsernameNotFoundException("등록된 유저가 없습니다."));
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
