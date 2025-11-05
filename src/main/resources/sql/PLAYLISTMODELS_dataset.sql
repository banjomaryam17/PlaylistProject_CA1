INSERT INTO genres (genreName, description) VALUES
('Pop','Catchy popular music for mainstream audiences'),
('Rock','Guitar-driven rock music from classic to modern'),
('Jazz','Smooth and improvisational music'),
('Hip Hop','Rap music and rhythmic beats'),
('Classical','Orchestral and symphonic works'),
('Country','Country music with storytelling lyrics'),
('Electronic','EDM, house, trance, and synth-based music'),
('Reggae','Reggae and island rhythms'),
('Blues','Soulful and emotional blues music'),
('Funk','Groovy funk tracks with strong rhythm'),
('Metal','Heavy metal and hard rock'),
('Soul','Soulful vocals and R&B influences'),
('R&B','Rhythm and blues with smooth vocals'),
('Punk','Fast-paced punk rock songs'),
('Folk','Acoustic and storytelling folk music'),
('Indie','Independent and alternative music'),
('Disco','Danceable disco hits'),
('Gospel','Religious and spiritual gospel music'),
('Latin','Latin rhythms and dance music'),
('K-Pop','Korean pop music with catchy tunes');

INSERT INTO playlists (userID, playlistName, description) VALUES
(1,'Morning Chill','Relaxing songs to start your day'),
(1,'Workout Pump','High-energy tracks for exercise'),
(2,'Road Trip','Songs perfect for a long drive'),
(2,'Evening Relax','Wind down with calm music'),
(3,'Party Hits','Top songs for parties'),
(3,'Classic Rock','Best rock hits of all time'),
(4,'Jazz Lounge','Smooth jazz for background music'),
(4,'Hip Hop Vibes','Popular rap and hip hop tracks'),
(5,'Study Focus','Concentration and focus music'),
(5,'Romantic Tunes','Love songs and ballads'),
(1,'Pop Favorites','Popular hits across years'),
(2,'Metal Madness','Hard rock and metal songs'),
(3,'Indie Mix','Independent and alternative songs'),
(4,'Disco Fever','Danceable disco tracks'),
(5,'Soulful Moments','Soul and R&B favorites'),
(1,'Electronic Beats','EDM and electronic music'),
(2,'Latin Party','Latin dance songs'),
(3,'K-Pop Hits','Top K-Pop tracks'),
(4,'Country Roads','Country classics'),
(5,'Folk Tales','Storytelling folk music');

INSERT INTO playlist_songs (playlistID, songID, addedAt) VALUES
 (1,1,NOW()),(1,2,NOW()),(1,12,NOW()),(1,14,NOW()),(1,20,NOW()),
 (2,2,NOW()),(2,7,NOW()),(2,8,NOW()),(2,11,NOW()),(2,17,NOW()),
 (3,3,NOW()),(3,4,NOW()),(3,5,NOW()),(3,6,NOW()),(3,13,NOW()),
 (4,9,NOW()),(4,10,NOW()),(4,1,NOW()),(4,12,NOW()),(4,19,NOW()),
 (5,14,NOW()),(5,15,NOW()),(5,16,NOW()),(5,3,NOW()),(5,7,NOW()),
 (6,3,NOW()),(6,4,NOW()),(6,17,NOW()),(6,18,NOW()),(6,20,NOW()),
 (7,5,NOW()),(7,6,NOW()),(7,8,NOW()),(7,12,NOW()),(7,13,NOW()),
 (8,2,NOW()),(8,9,NOW()),(8,10,NOW()),(8,11,NOW()),(8,14,NOW()),
 (9,15,NOW()),(9,16,NOW()),(9,1,NOW()),(9,4,NOW()),(9,7,NOW()),
 (10,8,NOW()),(10,9,NOW()),(10,10,NOW()),(10,12,NOW()),(10,19,NOW());


INSERT INTO `users` (`username`, `email`, `password`, `userType`)
VALUES (' juilt ', 'Amena@gmail.com', ' shen', 1),
       (' Zahidi', 'Zahidi@gmail.com', 'Dior', 1),
       (' Shoun', 'shoun@gmail.com', 'Dior', 1),
       ('Zain', 'Zain@gmail.com', 'dummy', 2);


INSERT INTO `artist` (`artistID`, `artistName`, `genre`, `hometown`, `dateOfBirth`)
VALUES (1, 'Taylor Swift', 'Pop', 'Nashville Tennessee', '1989-12-13'),
       (2, 'Drake', 'Rap', 'Toronto Canada', '1986-10-24'),
       (3, 'Billie Eilish', 'Alternative', 'Los Angeles California', '2001-12-18'),
       (4, 'The Weeknd', 'R&B', 'Toronto Canada', '1990-02-16'),
       (5, 'Bad Bunny', 'Reggaeton', 'San Juan Puerto Rico', '1994-03-10');