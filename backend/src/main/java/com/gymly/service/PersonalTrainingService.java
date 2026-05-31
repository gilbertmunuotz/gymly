package com.gymly.service;

import com.gymly.dto.BookPtRequest;
import com.gymly.dto.PtBookingResponse;
import com.gymly.dto.TrainerResponse;
import com.gymly.exception.BadRequestException;
import com.gymly.exception.ResourceNotFoundException;
import com.gymly.model.PtBooking;
import com.gymly.model.Trainer;
import com.gymly.repository.MembershipRepository;
import com.gymly.repository.PtBookingRepository;
import com.gymly.repository.TrainerRepository;
import com.gymly.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PersonalTrainingService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_ACTIVE = "ACTIVE";

    private final TrainerRepository trainerRepository;
    private final PtBookingRepository ptBookingRepository;
    private final MembershipRepository membershipRepository;

    public PersonalTrainingService(
            TrainerRepository trainerRepository,
            PtBookingRepository ptBookingRepository,
            MembershipRepository membershipRepository) {
        this.trainerRepository = trainerRepository;
        this.ptBookingRepository = ptBookingRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public List<TrainerResponse> getAvailableTrainers() {
        return trainerRepository.findByIsAvailableTrueOrderByFullNameAsc().stream()
                .map(this::toTrainerResponse)
                .toList();
    }

    @Transactional
    public PtBookingResponse bookSession(BookPtRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();

        if (!membershipRepository.existsByUserIdAndStatus(userId, STATUS_ACTIVE)) {
            throw new BadRequestException("Active membership required to book personal training");
        }

        if (request.getSessionDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Session date cannot be in the past");
        }

        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));

        if (!Boolean.TRUE.equals(trainer.getIsAvailable())) {
            throw new BadRequestException("Trainer is not available");
        }

        PtBooking booking = PtBooking.builder()
                .userId(userId)
                .trainer(trainer)
                .sessionDate(request.getSessionDate())
                .startTime(request.getStartTime())
                .status(STATUS_PENDING)
                .notes(request.getNotes())
                .build();

        PtBooking saved = ptBookingRepository.save(booking);
        return toBookingResponse(saved);
    }

    private TrainerResponse toTrainerResponse(Trainer trainer) {
        return TrainerResponse.builder()
                .id(trainer.getId())
                .fullName(trainer.getFullName())
                .specialty(trainer.getSpecialty())
                .bio(trainer.getBio())
                .available(Boolean.TRUE.equals(trainer.getIsAvailable()))
                .build();
    }

    private PtBookingResponse toBookingResponse(PtBooking booking) {
        return PtBookingResponse.builder()
                .bookingId(booking.getId())
                .trainerName(booking.getTrainer().getFullName())
                .sessionDate(booking.getSessionDate())
                .startTime(booking.getStartTime())
                .status(booking.getStatus())
                .notes(booking.getNotes())
                .build();
    }
}
