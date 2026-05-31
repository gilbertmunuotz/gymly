package com.gymly.repository;

import com.gymly.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByUserIdAndCheckedInAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    List<Attendance> findTop10ByUserIdOrderByCheckedInAtDesc(Long userId);
}
