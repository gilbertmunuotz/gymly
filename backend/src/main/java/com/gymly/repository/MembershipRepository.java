package com.gymly.repository;

import com.gymly.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    boolean existsByUserIdAndStatus(Long userId, String status);

    Optional<Membership> findByUserIdAndStatus(Long userId, String status);
}
