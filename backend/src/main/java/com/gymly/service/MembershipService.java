package com.gymly.service;

import com.gymly.dto.MembershipPlanResponse;
import com.gymly.dto.MembershipResponse;
import com.gymly.exception.BadRequestException;
import com.gymly.exception.ResourceNotFoundException;
import com.gymly.model.Membership;
import com.gymly.model.MembershipPlan;
import com.gymly.repository.MembershipPlanRepository;
import com.gymly.repository.MembershipRepository;
import com.gymly.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipService {

    private static final String STATUS_ACTIVE = "ACTIVE";

    private final MembershipPlanRepository planRepository;
    private final MembershipRepository membershipRepository;

    public MembershipService(
            MembershipPlanRepository planRepository,
            MembershipRepository membershipRepository) {
        this.planRepository = planRepository;
        this.membershipRepository = membershipRepository;
    }

    public List<MembershipPlanResponse> getAllPlans() {
        return planRepository.findAll().stream()
                .map(this::toPlanResponse)
                .toList();
    }

    public MembershipResponse getActiveMembership() {
        Long userId = SecurityUtils.getCurrentUserId();
        Membership membership = membershipRepository
                .findByUserIdAndStatus(userId, STATUS_ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active membership found"));
        return toMembershipResponse(membership);
    }

    @Transactional
    public MembershipResponse subscribe(Long planId) {
        Long userId = SecurityUtils.getCurrentUserId();

        if (membershipRepository.existsByUserIdAndStatus(userId, STATUS_ACTIVE)) {
            throw new BadRequestException("You already have an active membership");
        }

        MembershipPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership plan not found"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(plan.getDurationDays());

        Membership membership = Membership.builder()
                .userId(userId)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .status(STATUS_ACTIVE)
                .build();

        Membership saved = membershipRepository.save(membership);
        return toMembershipResponse(saved);
    }

    private MembershipPlanResponse toPlanResponse(MembershipPlan plan) {
        return MembershipPlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .durationDays(plan.getDurationDays())
                .build();
    }

    private MembershipResponse toMembershipResponse(Membership membership) {
        return MembershipResponse.builder()
                .id(membership.getId())
                .planName(membership.getPlan().getName())
                .startDate(membership.getStartDate())
                .endDate(membership.getEndDate())
                .status(membership.getStatus())
                .build();
    }
}
