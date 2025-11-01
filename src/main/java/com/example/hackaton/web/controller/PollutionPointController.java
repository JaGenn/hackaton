package com.example.hackaton.web.controller;

import com.example.hackaton.mapper.PollutionPointMapper;
import com.example.hackaton.repository.UserRepository;
import com.example.hackaton.service.UserService;
import com.example.hackaton.web.model.dto.PollutionPointRequestDto;
import com.example.hackaton.web.model.dto.PollutionPointResponseDto;
import com.example.hackaton.web.model.entity.User;
import com.example.hackaton.web.model.entity.pollution.PollutionPoint;
import com.example.hackaton.security.CustomUserDetails;
import com.example.hackaton.service.PollutionPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PollutionPointController {


    private final PollutionPointService pollutionPointService;
    private final PollutionPointMapper pollutionPointMapper;
    private final UserService userService;

    @GetMapping
    public List<PollutionPointResponseDto> getAllPollutionPoints() {
        List<PollutionPoint> pollutionPoints = pollutionPointService.getAllPollutionPoints();
        return pollutionPointMapper.toDto(pollutionPoints);
    }

    @PostMapping
    public PollutionPointResponseDto addPollutionPoint(@ModelAttribute PollutionPointRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        PollutionPoint createdPoint = pollutionPointService.createPollutionPoint(requestDto, userDetails.getUsername());
        return pollutionPointMapper.toDto(createdPoint);
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePollutionPoint(@PathVariable Long id, @RequestBody String status, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.getByEmail(userDetails.getUsername());
        pollutionPointService.updateStatus(id, status, user);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        pollutionPointService.delete(id);
    }

    @GetMapping("{id}")
    public PollutionPointResponseDto getById(@PathVariable Long id) {
        PollutionPoint point = pollutionPointService.getPollutionPointById(id);
        return pollutionPointMapper.toDto(point);
    }


}
