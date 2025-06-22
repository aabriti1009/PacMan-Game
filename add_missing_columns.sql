USE pacman_game;

-- Add the missing columns to the users table
ALTER TABLE users 
ADD COLUMN games_played INT DEFAULT 0,
ADD COLUMN high_score INT DEFAULT 0,
ADD COLUMN total_score BIGINT DEFAULT 0;

-- Update the registration_date column to be TIMESTAMP if it's not already
ALTER TABLE users MODIFY COLUMN registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP; 