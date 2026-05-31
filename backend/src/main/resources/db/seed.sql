-- =============================================================================
-- Gymly — Seed Data (Phase 2)
-- =============================================================================
-- Run after schema.sql against database: gymly
--
-- Demo login password for all seeded users: password123
-- (BCrypt hash from Spring BCryptPasswordEncoder)
-- =============================================================================

-- Users
INSERT INTO users (email, password_hash, full_name, phone) VALUES
    ('john@gmail.com',  '$2a$10$5TYSPXP3hA1gdqZTpkiubeBVsmUMq7GmswMxQWgNnyBaHcnvA5POe', 'John Kamau',   '0742345678'),
    ('jane@gmail.com',  '$2a$10$5TYSPXP3hA1gdqZTpkiubeBVsmUMq7GmswMxQWgNnyBaHcnvA5POe', 'Jane Robert', '0693913076'),
    ('peter@gmail.com', '$2a$10$5TYSPXP3hA1gdqZTpkiubeBVsmUMq7GmswMxQWgNnyBaHcnvA5POe', 'Peter Mathias','0734567890');

-- Membership plans (prices in KES for M-Pesa demo in Phase 6)
INSERT INTO membership_plans (name, description, price, duration_days) VALUES
    ('Basic',    'Gym floor access during off-peak hours',           1500.00, 30),
    ('Standard', 'Full gym access + 2 group classes per week',       2500.00, 30),
    ('Premium',  'Unlimited access, all classes, 1 PT session/month', 4500.00, 30);

-- Active memberships
INSERT INTO memberships (user_id, plan_id, start_date, end_date, status) VALUES
    (1, 3, CURRENT_DATE - INTERVAL '10 days', CURRENT_DATE + INTERVAL '20 days', 'ACTIVE'),
    (2, 2, CURRENT_DATE - INTERVAL '5 days',  CURRENT_DATE + INTERVAL '25 days', 'ACTIVE');

-- Trainers
INSERT INTO trainers (full_name, specialty, bio, image_url, is_available) VALUES
    ('David Mwangi',  'Strength & Conditioning', 'Certified strength coach with 5 years experience.',       NULL, TRUE),
    ('Grace Akyoo',  'Yoga & Flexibility',      'Yoga instructor focused on mobility and mindfulness.',    NULL, TRUE),
    ('Brian Hillary',  'HIIT & Cardio',           'High-energy HIIT sessions for fat loss and endurance.',     NULL, TRUE),
    ('Sarah Jacob',   'Personal Training',       'One-on-one coaching for beginners and advanced lifters.',   NULL, TRUE);

-- Group classes
INSERT INTO gym_classes (name, description, trainer_id, day_of_week, start_time, end_time, max_capacity, location) VALUES
    ('Morning Yoga',       'Start your day with stretching and breathing.',  2, 'MON', '07:00', '08:00', 15, 'Studio A'),
    ('Morning Yoga',       'Start your day with stretching and breathing.',  2, 'WED', '07:00', '08:00', 15, 'Studio A'),
    ('HIIT Blast',         '30-minute high-intensity interval training.',    3, 'TUE', '18:00', '18:45', 20, 'Main Floor'),
    ('HIIT Blast',         '30-minute high-intensity interval training.',    3, 'THU', '18:00', '18:45', 20, 'Main Floor'),
    ('Spin Cycle',         'Indoor cycling cardio session.',                 3, 'FRI', '17:30', '18:30', 12, 'Studio B'),
    ('Strength Basics',    'Intro to compound lifts and form.',              1, 'SAT', '09:00', '10:00', 10, 'Weight Room');

-- Class bookings
INSERT INTO class_bookings (user_id, gym_class_id, status) VALUES
    (1, 1, 'CONFIRMED'),
    (1, 3, 'CONFIRMED'),
    (2, 1, 'CONFIRMED');

-- Personal training bookings
INSERT INTO pt_bookings (user_id, trainer_id, session_date, start_time, status, notes) VALUES
    (1, 4, CURRENT_DATE + INTERVAL '3 days',  '10:00', 'CONFIRMED', 'Focus on squat form'),
    (2, 1, CURRENT_DATE + INTERVAL '5 days',  '14:00', 'PENDING',   'First PT session');

-- Attendance / check-ins
INSERT INTO attendance (user_id, checked_in_at, check_in_type) VALUES
    (1, CURRENT_TIMESTAMP - INTERVAL '2 days',  'GYM'),
    (1, CURRENT_TIMESTAMP - INTERVAL '1 day',   'GYM'),
    (2, CURRENT_TIMESTAMP - INTERVAL '3 hours', 'GYM');

-- Reset sequences so future inserts continue after seeded IDs
SELECT setval('users_id_seq',              (SELECT MAX(id) FROM users));
SELECT setval('membership_plans_id_seq',   (SELECT MAX(id) FROM membership_plans));
SELECT setval('memberships_id_seq',        (SELECT MAX(id) FROM memberships));
SELECT setval('trainers_id_seq',           (SELECT MAX(id) FROM trainers));
SELECT setval('gym_classes_id_seq',        (SELECT MAX(id) FROM gym_classes));
SELECT setval('class_bookings_id_seq',     (SELECT MAX(id) FROM class_bookings));
SELECT setval('pt_bookings_id_seq',        (SELECT MAX(id) FROM pt_bookings));
SELECT setval('attendance_id_seq',         (SELECT MAX(id) FROM attendance));
