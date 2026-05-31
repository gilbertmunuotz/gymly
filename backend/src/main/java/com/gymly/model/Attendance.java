package com.gymly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "checked_in_at", nullable = false)
    private LocalDateTime checkedInAt;

    @Column(name = "check_in_type", nullable = false, length = 20)
    private String checkInType;

    @PrePersist
    void onCreate() {
        if (checkedInAt == null) {
            checkedInAt = LocalDateTime.now();
        }
    }
}
