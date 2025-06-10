package com.music.joy.repo;

import com.music.joy.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findAllByArtistId(Long artistId);
}

