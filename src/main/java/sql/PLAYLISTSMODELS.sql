DROP DATABASE IF EXISTS PLAYLISTMODELS;
CREATE DATABASE IF NOT EXISTS PLAYLISTMODELS;
USE PLAYLISTMODELS;

-- Users table
DROP TABLE IF EXISTS users;
CREATE TABLE User (
user_id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) UNIQUE NOT NULL,
password_hash VARCHAR(255) NOT NULL,  Store BCrypt/SHA-256 hashed passwords!!!
    email VARCHAR(100) UNIQUE NOT NULL,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
last_login TIMESTAMP NULL,
is_active BOOLEAN DEFAULT TRUE,

    -- Indexes for performance
                        INDEX idx_username (username),
                        INDEX idx_email (email),
                        INDEX idx_is_active (is_active)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Songs table (dummy)
DROP TABLE IF EXISTS songs;
CREATE TABLE songs (
       songID INT(11) NOT NULL AUTO_INCREMENT,
       title VARCHAR(100) NOT NULL,
       artist VARCHAR(100) NOT NULL,
       album VARCHAR(100) DEFAULT NULL,
       genreID INT(11) DEFAULT NULL,
       duration INT(11) DEFAULT NULL, -- in seconds
       PRIMARY KEY (songID)
);

---- Artist table
DROP TABLE IF EXISTS users;
CREATE TABLE Artist (
artist_id INT PRIMARY KEY AUTO_INCREMENT,
artist_name VARCHAR(100) NOT NULL,
biography TEXT,
country VARCHAR(50),
formed_year INT,
website_url VARCHAR(255),
INDEX idx_artist_name (artist_name),
INDEX idx_country (country)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- Genres table
DROP TABLE IF EXISTS genres;
CREATE TABLE genres (
        genreID INT(11) NOT NULL AUTO_INCREMENT,
        genreName VARCHAR(50) NOT NULL,
        description VARCHAR(255) DEFAULT NULL,
        PRIMARY KEY (genreID)
);

-- Playlists table (FK removed temporarily)
DROP TABLE IF EXISTS playlists;
CREATE TABLE playlists (
       playlistID INT(11) NOT NULL AUTO_INCREMENT,
       userID INT(11) NOT NULL,
       playlistName VARCHAR(50) NOT NULL,
       description VARCHAR(255) DEFAULT NULL,
       createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
       PRIMARY KEY (playlistID)
);

-- Playlist_Songs table (FKs removed temporarily)
DROP TABLE IF EXISTS playlist_songs;
CREATE TABLE playlist_songs (
            playlistID INT(11) NOT NULL,
            songID INT(11) NOT NULL,
            addedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
            PRIMARY KEY (playlistID, songID)
);
