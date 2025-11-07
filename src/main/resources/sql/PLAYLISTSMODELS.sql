DROP DATABASE IF EXISTS PLAYLISTMODELS;
CREATE DATABASE IF NOT EXISTS PLAYLISTMODELS;
USE PLAYLISTMODELS;

-- USERS TABLE
-- Fields: username, password, email, userType
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    userID   INT(11)      NOT NULL AUTO_INCREMENT,
    username VARCHAR(55)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    userType INT(1)       NOT NULL DEFAULT 1 COMMENT '1 for user, 2 for admin',
    PRIMARY KEY (userID),
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- GENRES TABLE
-- Fields: genreID, genreName, description
DROP TABLE IF EXISTS genres;
CREATE TABLE genres
(
    genreID     INT(11)     NOT NULL AUTO_INCREMENT,
    genreName   VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (genreID)
);

-- ARTIST TABLE (matches Artist.java)
DROP TABLE IF EXISTS artist;
CREATE TABLE artist
(
    artistId    INT(11)      NOT NULL AUTO_INCREMENT,
    artistName  VARCHAR(100) NOT NULL,
    genre       VARCHAR(55) DEFAULT NULL,
    dateOfBirth DATE        DEFAULT NULL,
    PRIMARY KEY (artistId)
);

DROP TABLE IF EXISTS playlists;
CREATE TABLE playlists
(
    playlistID   INT(11)     NOT NULL AUTO_INCREMENT,
    userID       INT(11)     NOT NULL,
    playlistName VARCHAR(50) NOT NULL,
    description  VARCHAR(255) DEFAULT NULL,
    isPublic     BOOLEAN      DEFAULT 0,
    createdAt    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (playlistID),
    FOREIGN KEY (userID) REFERENCES users (userID) ON DELETE CASCADE
);

-- SONGS TABLE
DROP TABLE IF EXISTS songs;
CREATE TABLE songs
(
    songID   INT(11)      NOT NULL AUTO_INCREMENT,
    title    VARCHAR(100) NOT NULL,
    artist   VARCHAR(100) NOT NULL,
    album    VARCHAR(100) DEFAULT NULL,
    genreID  INT(11)      DEFAULT NULL,
    duration INT(11)      DEFAULT NULL,
    PRIMARY KEY (songID),
    FOREIGN KEY (genreID) REFERENCES genres (genreID) ON DELETE SET NULL
);

-- PLAYLIST_SONGS TABLE
DROP TABLE IF EXISTS playlist_songs;
CREATE TABLE playlist_songs
(
    playlistID INT(11) NOT NULL,
    songID     INT(11) NOT NULL,
    addedAt    DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (playlistID, songID),
    FOREIGN KEY (playlistID) REFERENCES playlists (playlistID) ON DELETE CASCADE,
    FOREIGN KEY (songID) REFERENCES songs (songID) ON DELETE CASCADE
);