DROP DATABASE IF EXISTS PLAYLISTMODELS;
CREATE DATABASE IF NOT EXISTS PLAYLISTMODELS;
USE PLAYLISTMODELS;

-- Users table (dummy)
DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       userID INT(11) NOT NULL AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (userID)
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
