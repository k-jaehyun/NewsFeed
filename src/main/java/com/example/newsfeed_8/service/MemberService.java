package com.example.newsfeed_8.service;

import com.example.newsfeed_8.dto.CommonResponseDto;
import com.example.newsfeed_8.dto.MemberDto;
import com.example.newsfeed_8.dto.MemberRequestDto;
import com.example.newsfeed_8.entity.Member;
import com.example.newsfeed_8.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MemberRequestDto memberRequestDto) {
        String userId = memberRequestDto.getUserId();
        String password = passwordEncoder.encode(memberRequestDto.getPassword());
        String email = memberRequestDto.getEmail();
        String introduction = memberRequestDto.getIntroduction();

        if (memberRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저 입니다.");
        }
        Member member = new Member(userId, password, email, introduction);
        memberRepository.save(member);
    }

    public void login(MemberRequestDto memberRequestDto) {
        String userId = memberRequestDto.getUserId();
        String password = memberRequestDto.getPassword();

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("등록된 유저가 없습니다."));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public CommonResponseDto updateEmail(Member member,
            MemberDto.UpdateEmailRequestDto dto) throws Exception {

        member.setEmail(dto.getEmail());
        memberRepository.save(member);

        return new CommonResponseDto(HttpStatus.OK.value(), "email update success");
    }

    public CommonResponseDto updatePassword(Member member,
            MemberDto.UpdatePasswordRequestDto dto) throws Exception {

        try {
            if (!passwordEncoder.matches(dto.getOriginPassword(), member.getPassword())) {
                throw new Exception("password update failed: Origin password not match");
            }
            if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
                throw new Exception("password update failed: New password not match");
            }
        } catch (Exception e) {
            return new CommonResponseDto(404, e.getMessage());
        }

        member.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        memberRepository.save(member);

        return new CommonResponseDto(HttpStatus.OK.value(), "password update success");
    }

    public CommonResponseDto updateIntroduction(Member member,
            MemberDto.UpdateIntroductionRequestDto dto) throws Exception {

        member.setIntroduction(dto.getIntroduction());
        memberRepository.save(member);

        return new CommonResponseDto(HttpStatus.OK.value(), "introduction update success");
    }
}
