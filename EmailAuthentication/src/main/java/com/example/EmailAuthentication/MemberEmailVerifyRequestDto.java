package com.example.EmailAuthentication;

import lombok.Data;

@Data
public class MemberEmailVerifyRequestDto {
    private String email;
    private String verificationCode;
    private boolean isVerified;
    private String message;
}
