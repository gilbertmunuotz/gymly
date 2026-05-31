package com.gymly.repository;

import com.gymly.model.PtBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PtBookingRepository extends JpaRepository<PtBooking, Long> {
}
