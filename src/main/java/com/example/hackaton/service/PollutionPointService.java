package com.example.hackaton.service;


import com.example.hackaton.repository.UserRepository;
import com.example.hackaton.web.model.dto.PollutionPointRequestDto;
import com.example.hackaton.web.model.entity.User;
import com.example.hackaton.web.model.entity.pollution.PollutionPoint;
import com.example.hackaton.web.model.entity.pollution.PollutionType;
import com.example.hackaton.web.model.entity.pollution.Role;
import com.example.hackaton.web.model.entity.pollution.Status;
import com.example.hackaton.repository.PollutionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PollutionPointService {

    private final PollutionRepository pollutionRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;

    public List<PollutionPoint> getAllPollutionPoints() {
        return pollutionRepository.findAll();
    }

    public PollutionPoint createPollutionPoint(PollutionPointRequestDto requestDto, String authorName) {
        PollutionPoint pollutionPoint = createEntity(requestDto, authorName);
        return pollutionRepository.save(pollutionPoint);
    }

    public void updateStatus(Long id, String status, User user) {
        PollutionPoint point = getPollutionPointById(id);
        Status newStatus = findStatus(status);

        point.setStatus(newStatus);
        pollutionRepository.save(point);

        if (newStatus == Status.CLEANED) {
            user.incrementCleanedCount();
            updateRole(user);
            userRepository.save(user);
        }
    }

    public void delete(Long id) {
        pollutionRepository.deleteById(id);
    }

    public PollutionPoint getPollutionPointById(Long id) {
        return pollutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Pollution Point by id " + id + " not found")
        );
    }

    private PollutionPoint createEntity(PollutionPointRequestDto requestDto, String authorName) {
        PollutionPoint pollutionPoint = new PollutionPoint();
        pollutionPoint.setLat(requestDto.getLat());
        pollutionPoint.setLon(requestDto.getLon());
        pollutionPoint.setDescription(requestDto.getDescription());
        pollutionPoint.setPollutionType(findPollutionType(requestDto.getPollutionType()));
        pollutionPoint.setStatus(Status.NEW);
        pollutionPoint.setPhotoUrl(imageService.uploadImage(requestDto.getImage()));
        pollutionPoint.setAuthor(authorName);
        pollutionPoint.setCreatedAt(LocalDateTime.now());
        pollutionRepository.save(pollutionPoint);
        return pollutionPoint;
    }

    private PollutionType findPollutionType(String pollutionType) {
        return switch (pollutionType) {
            case "Мусор" -> PollutionType.TRASH;
            case "Химические отходы" -> PollutionType.CHEMICAL_WASTE;
            case "Промышленные отходы" -> PollutionType.INDUSTRIAL_WASTE;
            default -> PollutionType.OTHER;
        };
    }

    private Status findStatus(String status) {
        return switch (status) {
            case "В процессе" -> Status.IN_PROGRESS;
            case "Убрано" -> Status.CLEANED;
            default -> Status.OTHER;
        };
    }

    private void updateRole(User user) {
        int cleaned = user.getCleanedCount();
        Role newRole = switch (cleaned) {
            case 0, 1, 2 -> Role.NOVICE;
            case 3, 4, 5, 6, 7, 8, 9 -> Role.HELPER;
            case 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 -> Role.ECO_HERO;
            case 20, 21, 22, 23, 24, 25, 26, 27, 28, 29 -> Role.MENTOR;
            default -> Role.AMBASSADOR;
        };
        user.setRole(newRole);
    }

}
