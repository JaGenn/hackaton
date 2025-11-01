package com.example.hackaton.repository;

import com.example.hackaton.web.model.entity.pollution.PollutionPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollutionRepository extends JpaRepository<PollutionPoint, Long> {
}
