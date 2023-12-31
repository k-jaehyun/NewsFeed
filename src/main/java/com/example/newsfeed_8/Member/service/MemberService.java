package com.example.newsfeed_8.Member.service;

import com.example.newsfeed_8.Comment.dto.CommonResponseDto;
import com.example.newsfeed_8.Member.dto.MemberDto;
import com.example.newsfeed_8.Member.dto.MemberRequestDto;
import com.example.newsfeed_8.Member.entity.Member;
import com.example.newsfeed_8.Member.repository.MemberRepository;
import com.example.newsfeed_8.jwt.Util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtUtil jwtUtil;


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

    public void logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String redisToken = token.substring(7);
        long remainingTime = jwtUtil.getRemainingTime(redisToken);
        if (remainingTime > 0) {
            jwtUtil.blacklistToken((redisToken));
            System.out.println("토큰 삭제");
        }
    }

    public MemberDto.GetMyAccountResponseDto getMyAccount(Member member) throws Exception {

        MemberDto.GetMyAccountResponseDto responseDto = MemberDto.GetMyAccountResponseDto.builder()
                .userId(member.getUserId())
                .email(member.getEmail())
                .introduction(member.getIntroduction())
                .build();

        return responseDto;
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
