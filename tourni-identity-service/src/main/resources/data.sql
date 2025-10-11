  -- AppUser
INSERT IGNORE INTO app_user(username, first_name, last_name, email, password, role, record_created_date, record_created_by, is_active)
VALUES
  ('admin', 'admin', 'admin', 'admin@gmail.com', '$2b$04$oZOGGm.sCkWzwqRAzuWpOezA709GaQs1KZV4xPh69DnnCD4GKSrdW', 'ADMIN', CURRENT_DATE(), UUID(), true);
