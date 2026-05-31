package com.gymly.repository;

import com.gymly.model.ClassBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {

    long countByGymClass_IdAndStatus(Long gymClassId, String status);

    boolean existsByUserIdAndGymClass_IdAndStatus(Long userId, Long gymClassId, String status);

    List<ClassBooking> findByUserIdAndStatus(Long userId, String status);
}
