  -- AppUser (for development/testing only)
-- Note: Password is 'admin@4789' for both users (BCrypt encoded)
INSERT IGNORE INTO app_user(username, first_name, last_name, email, password, token_version, record_created_date, record_created_by, is_active)
VALUES
  ('admin', 'Admin', 'User', 'admin@tournimate.com', '$2a$10$2GgsW45hrAFhmbNbojj3m./wpbEud9YK/LgX1SmkfHIQxpCUE4M/O', 0, CURRENT_DATE(), UUID(), true),
  ('user', 'Regular', 'User', 'user@tournimate.com', '$2a$10$2GgsW45hrAFhmbNbojj3m./wpbEud9YK/LgX1SmkfHIQxpCUE4M/O', 0, CURRENT_DATE(), UUID(), true);

-- User Roles
INSERT IGNORE INTO user_roles (username, role) VALUES 
  ('admin', 'ADMIN'),
  ('admin', 'USER'),
  ('user', 'USER');
