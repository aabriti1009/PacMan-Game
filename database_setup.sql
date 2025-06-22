-- PacMan Game Database Setup Script
-- Run this script in MySQL Workbench to set up the database

-- Create the database
CREATE DATABASE IF NOT EXISTS pacman_game;

-- Use the database
USE pacman_game;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    games_played INT DEFAULT 0,
    high_score INT DEFAULT 0,
    total_score BIGINT DEFAULT 0,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create game scores table
CREATE TABLE IF NOT EXISTS game_scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    score INT NOT NULL,
    level INT NOT NULL,
    play_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create game settings table
CREATE TABLE IF NOT EXISTS game_settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    sound_enabled BOOLEAN DEFAULT TRUE,
    music_enabled BOOLEAN DEFAULT TRUE,
    difficulty_level VARCHAR(20) DEFAULT 'NORMAL',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert some sample data (optional)
INSERT INTO users (username, email, password) VALUES 
('admin', 'admin@pacman.com', 'admin123'),
('player1', 'player1@pacman.com', 'player123'),
('player2', 'player2@pacman.com', 'player456');

-- Show the created tables
SHOW TABLES;

-- Show table structures
DESCRIBE users;
DESCRIBE game_scores;
DESCRIBE game_settings; 