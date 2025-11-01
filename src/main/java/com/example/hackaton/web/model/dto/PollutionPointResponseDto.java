package com.example.hackaton.web.model.dto;

public record PollutionPointResponseDto(Long id, double lat, double lon, String description, String pollutionType, String status, String createdAt) {
}
