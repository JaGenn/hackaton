package com.example.hackaton.mapper;

import com.example.hackaton.web.model.dto.PollutionPointResponseDto;
import com.example.hackaton.web.model.entity.pollution.PollutionPoint;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PollutionPointMapper {

    public PollutionPointResponseDto toDto(PollutionPoint entity) {
        return new PollutionPointResponseDto(
                entity.getId(),
                entity.getLat(),
                entity.getLon(),
                entity.getDescription(),
                entity.getPollutionType().name(),
                entity.getStatus().name(),
                prepareDate(entity.getCreatedAt())
        );
    }

    public List<PollutionPointResponseDto> toDto(List<PollutionPoint> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    private String prepareDate(LocalDateTime date) {
        String dateStr = date.toString();
        return dateStr.substring(0,10) + " " + dateStr.substring(11, 19);
    }
}
