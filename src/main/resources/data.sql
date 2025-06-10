-- Insert Artists
INSERT INTO artist (name, external_identifier)
SELECT 'Taylor Swift', 'talor-swift'
WHERE NOT EXISTS (
    SELECT 1 FROM artist WHERE external_identifier = 'talor-swift'
);

INSERT INTO artist (name, external_identifier)
SELECT 'bon-jovi', 'bon-jovi'
WHERE NOT EXISTS (
    SELECT 1 FROM artist WHERE external_identifier = 'bon-jovi'
);

INSERT INTO artist (name, external_identifier)
SELECT 'sonu nigam', 'sonu-nigam'
WHERE NOT EXISTS (
    SELECT 1 FROM artist WHERE external_identifier = 'sonu-nigam'
);

-- Insert Tracks
INSERT INTO track (title, genre, length_in_seconds, artist_id)
SELECT 'Love Story', 'Pop', 230, 2
WHERE NOT EXISTS (
    SELECT 1 FROM track WHERE title = 'Love Story' AND artist_id = 1
);

INSERT INTO track (title, genre, length_in_seconds, artist_id)
SELECT 'Shake It Off', 'Pop', 242, 2
WHERE NOT EXISTS (
    SELECT 1 FROM track WHERE title = 'Shake It Off' AND artist_id = 1
);

INSERT INTO track (title, genre, length_in_seconds, artist_id)
SELECT 'trere bin', 'Hip-Hop', 267, 3
WHERE NOT EXISTS (
    SELECT 1 FROM track WHERE title = 'trere bin' AND artist_id = 3
);

