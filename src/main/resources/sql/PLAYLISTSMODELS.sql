DROP DATABASE IF EXISTS PLAYLISTMODELS;
CREATE DATABASE IF NOT EXISTS PLAYLISTMODELS;
USE PLAYLISTMODELS;

-- USERS TABLE
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
DROP TABLE IF EXISTS genres;
CREATE TABLE genres
(
    genreID     INT(11)     NOT NULL AUTO_INCREMENT,
    genreName   VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (genreID)
);

-- ARTIST TABLE
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

-- ALBUM TABLE
DROP TABLE IF EXISTS album;
CREATE TABLE album
(
    album_id INT PRIMARY KEY AUTO_INCREMENT,
    artist_id INT NOT NULL,
    album_title VARCHAR(150) NOT NULL,
    release_year INT,
    album_art_url VARCHAR(255),
    total_tracks INT DEFAULT 0,

    CONSTRAINT fk_album_artist
    FOREIGN KEY (artist_id)
    REFERENCES Artist(artistId)
    ON DELETE CASCADE
    ON UPDATE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- SONGS TABLE
DROP TABLE IF EXISTS songs;
CREATE TABLE songs
(
   song_id INT PRIMARY KEY AUTO_INCREMENT,

    album_id INT NOT NULL,
    artist_id INT NOT NULL,

    song_title VARCHAR(150) NOT NULL,
    duration_seconds INT,
    track_number INT,
    release_year INT,

    CONSTRAINT fk_song_album
    FOREIGN KEY (album_id)
    REFERENCES Album(album_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

    CONSTRAINT fk_song_artist
    FOREIGN KEY (artist_id)
    REFERENCES Artist(artistId)
    ON DELETE CASCADE
    ON UPDATE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- PLAYLIST_SONGS TABLE
DROP TABLE IF EXISTS playlist_songs;
CREATE TABLE playlist_songs
(
    playlistID INT(11) NOT NULL,
    songID     INT(11) NOT NULL,
    addedAt    DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (playlistID, songID),
    FOREIGN KEY (playlistID) REFERENCES playlists (playlistID) ON DELETE CASCADE,
    FOREIGN KEY (songID) REFERENCES songs (song_id) ON DELETE CASCADE
);