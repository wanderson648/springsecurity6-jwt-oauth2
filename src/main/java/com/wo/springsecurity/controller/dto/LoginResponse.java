package com.wo.springsecurity.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
