package com.example.newsfeed_8.controller;

import com.example.newsfeed_8.dto.CommonResponseDto;
import com.example.newsfeed_8.dto.MemberDto;
import com.example.newsfeed_8.dto.MemberRequestDto;
import com.example.newsfeed_8.jwt.JwtUtil;
import com.example.newsfeed_8.security.MemberDetailsImpl;
import com.example.newsfeed_8.service.EmailAuthService;
import com.example.newsfeed_8.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailAuthService emailAuthService;
    private final JwtUtil jwtUtil;

    @PostMapping("signup")
    public ResponseEntity<CommonResponseDto> signup(
            @Valid @RequestBody MemberRequestDto memberRequestDto) {
        try {
            memberService.signup(memberRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDto("중복된 아이디 입니다.", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new CommonResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

    @PostMapping("email/send")
    public String emailSend(@RequestBody MemberDto.SendEmailRequestDto requestDto)
            throws MessagingException, UnsupportedEncodingException {

        String authCode = emailAuthService.sendEmail(requestDto.getEmail());
        return authCode;
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto> login(@RequestBody MemberRequestDto memberRequestDto,
            HttpServletResponse res) {
        try {
            memberService.login(memberRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        res.setHeader(JwtUtil.AUTHORIZATION_HEADER,
                jwtUtil.createToken(memberRequestDto.getUserId()));

        return ResponseEntity.ok().body(new CommonResponseDto("로그인 성공", HttpStatus.OK.value()));
    }

    @GetMapping("")
    public ResponseEntity<MemberDto.GetMyAccountResponseDto> getMyAccount(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws Exception {

        return ResponseEntity.ok()
                .body(memberService.getMyAccount(memberDetails.getMember()));
    }

    @PatchMapping("/email")
    public ResponseEntity<CommonResponseDto> updateEmail(
            @RequestBody MemberDto.UpdateEmailRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws Exception {

        return ResponseEntity.ok()
                .body(memberService.updateEmail(memberDetails.getMember(), requestDto));
    }

    @PatchMapping("/password")
    public ResponseEntity<CommonResponseDto> updatePassword(
            @RequestBody MemberDto.UpdatePasswordRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws Exception {

        return ResponseEntity.ok()
                .body(memberService.updatePassword(memberDetails.getMember(), requestDto));
    }

    @PatchMapping("/introduction")
    public ResponseEntity<CommonResponseDto> updateIntroduction(
            @RequestBody MemberDto.UpdateIntroductionRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws Exception {

        return ResponseEntity.ok()
                .body(memberService.updateIntroduction(memberDetails.getMember(), requestDto));
    }

}
