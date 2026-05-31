package com.gymly.service;

import com.gymly.dto.AttendanceResponse;
import com.gymly.dto.CheckInRequest;
import com.gymly.exception.BadRequestException;
import com.gymly.model.Attendance;
import com.gymly.repository.AttendanceRepository;
import com.gymly.repository.MembershipRepository;
import com.gymly.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String TYPE_GYM = "GYM";

    private final AttendanceRepository attendanceRepository;
    private final MembershipRepository membershipRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            MembershipRepository membershipRepository) {
        this.attendanceRepository = attendanceRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getRecentCheckIns() {
        Long userId = SecurityUtils.getCurrentUserId();
        return attendanceRepository.findTop10ByUserIdOrderByCheckedInAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AttendanceResponse checkIn(CheckInRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();

        if (!membershipRepository.existsByUserIdAndStatus(userId, STATUS_ACTIVE)) {
            throw new BadRequestException("Active membership required to check in");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        if (attendanceRepository.existsByUserIdAndCheckedInAtBetween(userId, startOfDay, endOfDay)) {
            throw new BadRequestException("You have already checked in today");
        }

        String checkInType = request.getCheckInType() != null ? request.getCheckInType() : TYPE_GYM;

        Attendance attendance = Attendance.builder()
                .userId(userId)
                .checkInType(checkInType)
                .build();

        Attendance saved = attendanceRepository.save(attendance);
        return toResponse(saved);
    }

    private AttendanceResponse toResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .checkedInAt(attendance.getCheckedInAt())
                .checkInType(attendance.getCheckInType())
                .build();
    }
}
