package com.gymly.security;

/**
 * Represents an authenticated user extracted from a JWT.
 * Used as the SecurityContext principal in Phase 4+.
 */
public record AuthenticatedUser(String email, Long userId) {
}
