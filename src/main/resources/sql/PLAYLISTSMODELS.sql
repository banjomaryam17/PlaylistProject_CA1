DROP DATABASE IF EXISTS PLAYLISTMODELS;
CREATE DATABASE IF NOT EXISTS PLAYLISTMODELS;
USE PLAYLISTMODELS;

--- user
DROP TABLE IF EXISTS playlists;
CREATE TABLE users
(
    username varchar(55)         NOT NULL,
    userID INT(11) NOT NULL AUTO_INCREMENT UNIQUE,
    email    varchar(255) NOT NULL,
    password varchar(255)        NOT NULL,
    userType int(1) NOT NULL DEFAULT 1 COMMENT '1 for user, 2 for admin ',
    PRIMARY KEY (username)
);

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

DROP TABLE IF EXISTS playlists;
CREATE TABLE artist
(
    artistID    int(255)    NOT NULL,
    artistName  varchar(55) NOT NULL,
    genre       varchar(55) NOT NULL,
    dateOfBirth DATE,
    PRIMARY KEY (artistID)
);


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
