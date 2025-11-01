package com.example.hackaton.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollutionPointRequestDto {

    private double lat;
    private double lon;
    private String description;
    private String pollutionType;
    private MultipartFile image;
}
