-- =============================================================================
-- Gymly — PostgreSQL Schema (Phase 2)
-- =============================================================================
-- Run order: schema.sql first, then seed.sql
--
-- ERD overview:
--   users ──┬── memberships ── membership_plans
--           ├── class_bookings ── gym_classes ── trainers
--           ├── pt_bookings ── trainers
--           └── attendance
--
-- Naming: snake_case tables (plural), id primary keys, *_id foreign keys
-- =============================================================================

-- Drop in reverse dependency order (safe re-run during development)
DROP TABLE IF EXISTS attendance CASCADE;
DROP TABLE IF EXISTS pt_bookings CASCADE;
DROP TABLE IF EXISTS class_bookings CASCADE;
DROP TABLE IF EXISTS gym_classes CASCADE;
DROP TABLE IF EXISTS memberships CASCADE;
DROP TABLE IF EXISTS membership_plans CASCADE;
DROP TABLE IF EXISTS trainers CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ---------------------------------------------------------------------------
-- users — app members (auth + profile)
-- ---------------------------------------------------------------------------
CREATE TABLE users (
    id              BIGSERIAL       PRIMARY KEY,
    email           VARCHAR(255)    NOT NULL UNIQUE,
    password_hash   VARCHAR(255)    NOT NULL,
    full_name       VARCHAR(100)    NOT NULL,
    phone           VARCHAR(20),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------------
-- membership_plans — available subscription tiers
-- ---------------------------------------------------------------------------
CREATE TABLE membership_plans (
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    description     TEXT,
    price           DECIMAL(10, 2)  NOT NULL,
    duration_days   INT             NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------------
-- memberships — user subscriptions to a plan
-- ---------------------------------------------------------------------------
CREATE TABLE memberships (
    id              BIGSERIAL       PRIMARY KEY,
    user_id         BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    plan_id         BIGINT          NOT NULL REFERENCES membership_plans (id),
    start_date      DATE            NOT NULL,
    end_date        DATE            NOT NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE'
                    CHECK (status IN ('ACTIVE', 'EXPIRED', 'CANCELLED')),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------------
-- trainers — personal trainers (separate from users for simpler demo data)
-- ---------------------------------------------------------------------------
CREATE TABLE trainers (
    id              BIGSERIAL       PRIMARY KEY,
    full_name       VARCHAR(100)    NOT NULL,
    specialty       VARCHAR(100),
    bio             TEXT,
    image_url       VARCHAR(500),
    is_available    BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------------
-- gym_classes — scheduled group fitness classes
-- ---------------------------------------------------------------------------
CREATE TABLE gym_classes (
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    description     TEXT,
    trainer_id      BIGINT          REFERENCES trainers (id) ON DELETE SET NULL,
    day_of_week     VARCHAR(10)     NOT NULL
                    CHECK (day_of_week IN ('MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN')),
    start_time      TIME            NOT NULL,
    end_time        TIME            NOT NULL,
    max_capacity    INT             NOT NULL DEFAULT 20,
    location        VARCHAR(100),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (end_time > start_time)
);

-- ---------------------------------------------------------------------------
-- class_bookings — member reservations for group classes
-- ---------------------------------------------------------------------------
CREATE TABLE class_bookings (
    id              BIGSERIAL       PRIMARY KEY,
    user_id         BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    gym_class_id    BIGINT          NOT NULL REFERENCES gym_classes (id) ON DELETE CASCADE,
    booked_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          VARCHAR(20)     NOT NULL DEFAULT 'CONFIRMED'
                    CHECK (status IN ('CONFIRMED', 'CANCELLED')),
    UNIQUE (user_id, gym_class_id)
);

-- ---------------------------------------------------------------------------
-- pt_bookings — personal training session requests
-- ---------------------------------------------------------------------------
CREATE TABLE pt_bookings (
    id              BIGSERIAL       PRIMARY KEY,
    user_id         BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    trainer_id      BIGINT          NOT NULL REFERENCES trainers (id) ON DELETE CASCADE,
    session_date    DATE            NOT NULL,
    start_time      TIME            NOT NULL,
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING'
                    CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED')),
    notes           TEXT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------------
-- attendance — gym check-ins
-- ---------------------------------------------------------------------------
CREATE TABLE attendance (
    id              BIGSERIAL       PRIMARY KEY,
    user_id         BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    checked_in_at   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    check_in_type   VARCHAR(20)     NOT NULL DEFAULT 'GYM'
                    CHECK (check_in_type IN ('GYM', 'CLASS'))
);

-- ---------------------------------------------------------------------------
-- Indexes — simple lookups for common queries
-- ---------------------------------------------------------------------------
CREATE INDEX idx_users_email              ON users (email);

CREATE INDEX idx_memberships_user_id      ON memberships (user_id);
CREATE INDEX idx_memberships_status       ON memberships (status);
CREATE INDEX idx_memberships_user_status  ON memberships (user_id, status);

CREATE INDEX idx_gym_classes_trainer_id   ON gym_classes (trainer_id);
CREATE INDEX idx_gym_classes_day          ON gym_classes (day_of_week);

CREATE INDEX idx_class_bookings_user_id   ON class_bookings (user_id);
CREATE INDEX idx_class_bookings_class_id  ON class_bookings (gym_class_id);

CREATE INDEX idx_pt_bookings_user_id      ON pt_bookings (user_id);
CREATE INDEX idx_pt_bookings_trainer_id   ON pt_bookings (trainer_id);
CREATE INDEX idx_pt_bookings_date         ON pt_bookings (session_date);

CREATE INDEX idx_attendance_user_id       ON attendance (user_id);
CREATE INDEX idx_attendance_checked_in    ON attendance (checked_in_at);
