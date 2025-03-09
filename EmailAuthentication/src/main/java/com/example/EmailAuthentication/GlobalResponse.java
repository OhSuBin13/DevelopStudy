package com.example.EmailAuthentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class GlobalResponse<T>{
    private String code;
    private String message;
    private T data;

    // 성공 응답 (데이터 포함 X)
    public static <T> GlobalResponse<T> of(String code, String message) {
        return new GlobalResponse<>(code, message, null);
    }

    // 성공 응답 (데이터 포함 O)
    public static <T> GlobalResponse<T> of(String code, String message, T data) {
        return new GlobalResponse<>(code, message, data);
    }
}
