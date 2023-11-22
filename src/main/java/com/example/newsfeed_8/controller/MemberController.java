package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.CommonResponseDto;
import com.example.newsfeed_8.dto.MeberRequestDto;
import com.example.newsfeed_8.jwt.JwtUtil;
import com.example.newsfeed_8.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    @PostMapping("/no-auth/member")
    public ResponseEntity<CommonResponseDto> signup(@Valid @RequestBody MeberRequestDto meberRequestDto){
        try {
            memberService.signup(meberRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDto("중복된 아이디 입니다.", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new CommonResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }
    @PostMapping("/no-auth/member/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody MeberRequestDto meberRequestDto, HttpServletResponse res){
        try {
            memberService.login(meberRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        res.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(meberRequestDto.getUserId()));

        return ResponseEntity.ok().body(new CommonResponseDto("로그인 성공", HttpStatus.OK.value()));
    }
}
