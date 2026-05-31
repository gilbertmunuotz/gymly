package com.gymly.repository;

import com.gymly.model.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {
}
