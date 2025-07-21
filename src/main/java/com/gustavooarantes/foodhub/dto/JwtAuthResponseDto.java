package com.gustavooarantes.foodhub.dto;

public record JwtAuthResponseDto(
        String accessToken,
        String tokenType
) {
    public JwtAuthResponseDto(String accessToken) {
        this(accessToken, "Bearer");
    }
}