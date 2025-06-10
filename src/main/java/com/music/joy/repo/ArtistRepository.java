package com.music.joy.repo;

import com.music.joy.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByExternalIdentifier(String identifier);

}
