package com.example.EmailAuthentication;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;

    //인증 번호 전송
    @PostMapping("/sendEmail")
    public GlobalResponse<MemberEmailRequestDto> sendEmail(@RequestBody MemberEmailRequestDto requestDto) {
        memberService.sendCodeToEmail(requestDto.getEmail());
        return GlobalResponse.of("200", "이메일 전송 성공");
    }

    @PostMapping("/verifyEmail")
    public GlobalResponse<MemberEmailVerifyRequestDto> verifyEmail(@RequestBody MemberEmailVerifyRequestDto requestDto) {
        boolean isVerified = memberService.verifyCode(requestDto.getEmail(), requestDto.getVerificationCode());
        MemberEmailVerifyRequestDto responseDto = new MemberEmailVerifyRequestDto();
        responseDto.setVerified(isVerified);
        responseDto.setMessage(isVerified ? "Email verifed successfully" : "Invalid or expired verification code.");
        if(isVerified) return GlobalResponse.of("200", "인증 완료", responseDto);
        else return GlobalResponse.of("200", "인증 실패", responseDto);
    }
}
