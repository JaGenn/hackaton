package com.example.hackaton.web.model.entity.pollution;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pollution_point")
@Getter
@Setter
@NoArgsConstructor
public class PollutionPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private double lat;

    @Column(name = "longitude", nullable = false)
    private double lon;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PollutionType pollutionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String photoUrl;

    private String author;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
