-- Migration to support multiple roles per user and token versioning
-- This migration safely converts from single role to multiple roles

-- Step 1: Add token_version column to app_user table
ALTER TABLE app_user 
ADD COLUMN token_version INT NOT NULL DEFAULT 0;

-- Step 2: Create user_roles junction table for many-to-many relationship
CREATE TABLE IF NOT EXISTS user_roles (
    username VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (username, role),
    CONSTRAINT fk_user_roles_username 
        FOREIGN KEY (username) 
        REFERENCES app_user(username) 
        ON DELETE CASCADE
);

-- Step 3: Migrate existing single role data to new structure
-- Copy existing roles from app_user.role to user_roles table
INSERT INTO user_roles (username, role)
SELECT username, role
FROM app_user
WHERE role IS NOT NULL;

-- Step 4: Remove old role column (optional - uncomment if you want to remove it)
-- ALTER TABLE app_user DROP COLUMN role;

-- Note: If you keep the old 'role' column, your old code will still work
-- The @ElementCollection in JPA will use the user_roles table
-- You can remove the 'role' column later after confirming everything works

-- Step 5: Create index for better query performance
CREATE INDEX idx_user_roles_username ON user_roles(username);
CREATE INDEX idx_user_roles_role ON user_roles(role);

-- Step 6: Add some sample users with multiple roles (optional)
-- Uncomment below to add test users

-- Add admin user with both ADMIN and USER roles
-- INSERT IGNORE INTO app_user(username, first_name, last_name, email, password, token_version, record_created_date, record_created_by, is_active)
-- VALUES ('superadmin', 'Super', 'Admin', 'superadmin@tournimate.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 0, CURRENT_DATE(), UUID(), true);

-- INSERT INTO user_roles (username, role) VALUES 
-- ('superadmin', 'ADMIN'),
-- ('superadmin', 'USER');

-- Add regular user with USER role only
-- INSERT IGNORE INTO app_user(username, first_name, last_name, email, password, token_version, record_created_date, record_created_by, is_active)
-- VALUES ('testuser', 'Test', 'User', 'testuser@tournimate.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 0, CURRENT_DATE(), UUID(), true);

-- INSERT INTO user_roles (username, role) VALUES ('testuser', 'USER');

