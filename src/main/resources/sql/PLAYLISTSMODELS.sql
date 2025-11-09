DROP DATABASE IF EXISTS playlistsmodels;
CREATE DATABASE IF NOT EXISTS playlistsmodels;
USE playlistsmodels;

-- USERS TABLE
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    user_id   INT(11)      NOT NULL AUTO_INCREMENT,
    username  VARCHAR(55)  NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL UNIQUE,
    user_type TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '1 for user, 2 for admin',
    PRIMARY KEY (user_id),
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- GENRES TABLE
DROP TABLE IF EXISTS genres;
CREATE TABLE genres
(
    genre_id    INT(11)     NOT NULL AUTO_INCREMENT,
    genre_name  VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (genre_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ARTIST TABLE (lowercase consistent)
DROP TABLE IF EXISTS artist;
CREATE TABLE artist
(
    artist_id     INT(11)      NOT NULL AUTO_INCREMENT,
    artist_name   VARCHAR(100) NOT NULL,
    genre         VARCHAR(55) DEFAULT NULL,
    date_of_birth DATE        DEFAULT NULL,
    PRIMARY KEY (artist_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ALBUM TABLE
DROP TABLE IF EXISTS album;
CREATE TABLE album
(
    album_id      INT(11)      NOT NULL AUTO_INCREMENT,
    artist_id     INT(11)      NOT NULL,
    album_title   VARCHAR(150) NOT NULL,
    release_year  INT,
    album_art_url VARCHAR(255),
    total_tracks  INT DEFAULT 0,
    PRIMARY KEY (album_id),
    CONSTRAINT fk_album_artist
        FOREIGN KEY (artist_id)
            REFERENCES artist (artist_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- SONGS TABLE
DROP TABLE IF EXISTS songs;
CREATE TABLE songs
(
    song_id          INT(11)      NOT NULL AUTO_INCREMENT,
    album_id         INT(11)      NOT NULL,
    artist_id        INT(11)      NOT NULL,
    song_title       VARCHAR(150) NOT NULL,
    duration_seconds INT,
    track_number     INT,
    release_year     INT,
    PRIMARY KEY (song_id),
    CONSTRAINT fk_song_album
        FOREIGN KEY (album_id)
            REFERENCES album (album_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_song_artist
        FOREIGN KEY (artist_id)
            REFERENCES artist (artist_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- PLAYLISTS TABLE (after users)
DROP TABLE IF EXISTS playlists;
CREATE TABLE playlists
(
    playlist_id   INT(11)     NOT NULL AUTO_INCREMENT,
    user_id       INT(11)     NOT NULL,
    playlist_name VARCHAR(50) NOT NULL,
    description   VARCHAR(255) DEFAULT NULL,
    is_public     BOOLEAN      DEFAULT 0,
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (playlist_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- PLAYLIST_SONGS TABLE
DROP TABLE IF EXISTS playlist_songs;
CREATE TABLE playlist_songs
(
    playlist_id INT(11) NOT NULL,
    song_id     INT(11) NOT NULL,
    added_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (playlist_id, song_id),
    FOREIGN KEY (playlist_id) REFERENCES playlists (playlist_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs (song_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- CREDITCARD TABLE
DROP TABLE IF EXISTS creditcard;
CREATE TABLE creditcard
(
    card_id   INT(11)          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(55)      NOT NULL,
    last4     CHAR(4)          NOT NULL,
    brand     VARCHAR(30)      NOT NULL,
    exp_month TINYINT UNSIGNED NOT NULL,
    exp_year  SMALLINT         NOT NULL,
    FOREIGN KEY (username) REFERENCES users (username)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- RATING TABLE
DROP TABLE IF EXISTS rating;
CREATE TABLE rating
(
    rating_id     INT(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    song_id       INT(11)     NOT NULL,
    username      VARCHAR(55) NOT NULL,
    current_score TINYINT     NOT NULL,
    max_score     TINYINT     NOT NULL,
    CONSTRAINT fk_rating_song
        FOREIGN KEY (song_id) REFERENCES songs (song_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_rating_user
        FOREIGN KEY (username) REFERENCES users (username)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
