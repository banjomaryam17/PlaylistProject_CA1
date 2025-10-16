DROP DATABASE IF EXISTS PLAYLISTMODELS;
CREATE DATABASE IF NOT EXISTS PLAYLISTMODELS;
USE PLAYLISTMODELS;

-- Users table
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    username varchar(50)         NOT NULL,
    email    varchar(255) UNIQUE NOT NULL,
    password varchar(255)        NOT NULL,
    userType int(1) NOT NULL DEFAULT 1 COMMENT '1 for general user, 2 for admin ',
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
CREATE TABLE artist
(
    artistID    int(255)    NOT NULL,
    artistName  varchar(50) NOT NULL,
    genre       varchar(50) NOT NULL,
    hometown    varchar(50) NOT NULL,
    dateOfBirth  DATE,
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
