package com.gymly.service;

import com.gymly.dto.BookClassRequest;
import com.gymly.dto.ClassBookingResponse;
import com.gymly.dto.GymClassResponse;
import com.gymly.exception.BadRequestException;
import com.gymly.exception.ResourceNotFoundException;
import com.gymly.model.ClassBooking;
import com.gymly.model.GymClass;
import com.gymly.repository.ClassBookingRepository;
import com.gymly.repository.GymClassRepository;
import com.gymly.repository.MembershipRepository;
import com.gymly.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GymClassService {

    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_ACTIVE = "ACTIVE";

    private final GymClassRepository gymClassRepository;
    private final ClassBookingRepository classBookingRepository;
    private final MembershipRepository membershipRepository;

    public GymClassService(
            GymClassRepository gymClassRepository,
            ClassBookingRepository classBookingRepository,
            MembershipRepository membershipRepository) {
        this.gymClassRepository = gymClassRepository;
        this.classBookingRepository = classBookingRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public List<GymClassResponse> getAllClasses() {
        Long userId = SecurityUtils.getCurrentUserId();
        return gymClassRepository.findAll().stream()
                .map(gymClass -> toResponse(gymClass, userId))
                .toList();
    }

    @Transactional
    public ClassBookingResponse bookClass(BookClassRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();

        if (!membershipRepository.existsByUserIdAndStatus(userId, STATUS_ACTIVE)) {
            throw new BadRequestException("Active membership required to book a class");
        }

        GymClass gymClass = gymClassRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        if (classBookingRepository.existsByUserIdAndGymClass_IdAndStatus(
                userId, gymClass.getId(), STATUS_CONFIRMED)) {
            throw new BadRequestException("You have already booked this class");
        }

        long confirmedCount = classBookingRepository.countByGymClass_IdAndStatus(
                gymClass.getId(), STATUS_CONFIRMED);
        if (confirmedCount >= gymClass.getMaxCapacity()) {
            throw new BadRequestException("This class is fully booked");
        }

        ClassBooking booking = ClassBooking.builder()
                .userId(userId)
                .gymClass(gymClass)
                .status(STATUS_CONFIRMED)
                .build();

        ClassBooking saved = classBookingRepository.save(booking);
        return toBookingResponse(saved);
    }

    private GymClassResponse toResponse(GymClass gymClass, Long userId) {
        long confirmedCount = classBookingRepository.countByGymClass_IdAndStatus(
                gymClass.getId(), STATUS_CONFIRMED);
        int spotsRemaining = Math.max(0, gymClass.getMaxCapacity() - (int) confirmedCount);
        boolean booked = classBookingRepository.existsByUserIdAndGymClass_IdAndStatus(
                userId, gymClass.getId(), STATUS_CONFIRMED);

        String trainerName = gymClass.getTrainer() != null
                ? gymClass.getTrainer().getFullName()
                : "TBA";

        return GymClassResponse.builder()
                .id(gymClass.getId())
                .name(gymClass.getName())
                .description(gymClass.getDescription())
                .trainerName(trainerName)
                .dayOfWeek(gymClass.getDayOfWeek())
                .startTime(gymClass.getStartTime())
                .endTime(gymClass.getEndTime())
                .location(gymClass.getLocation())
                .maxCapacity(gymClass.getMaxCapacity())
                .spotsRemaining(spotsRemaining)
                .bookedByUser(booked)
                .build();
    }

    private ClassBookingResponse toBookingResponse(ClassBooking booking) {
        GymClass gymClass = booking.getGymClass();
        return ClassBookingResponse.builder()
                .bookingId(booking.getId())
                .className(gymClass.getName())
                .dayOfWeek(gymClass.getDayOfWeek())
                .startTime(gymClass.getStartTime().toString())
                .location(gymClass.getLocation())
                .bookedAt(booking.getBookedAt())
                .build();
    }
}
