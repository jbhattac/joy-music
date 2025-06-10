package com.music.joy.service;

import com.music.joy.controller.dto.DtoMapper;
import com.music.joy.controller.dto.TrackRequestDto;
import com.music.joy.controller.dto.TrackResponseDto;
import com.music.joy.exception.ArtistNotFoundException;
import com.music.joy.model.Artist;
import com.music.joy.model.Track;
import com.music.joy.repo.ArtistRepository;
import com.music.joy.repo.TrackRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Creating a new track
 * Fetching tracks for an artist
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackService {

    private final TrackRepository trackRepo;
    private final ArtistRepository artistRepo;

    @CacheEvict(value = "tracksByArtist", key = "#identifier")
    public Track addTrack(String identifier, TrackRequestDto req) {
        Artist artist = artistRepo.findByExternalIdentifier(identifier)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found with identifier: " + identifier));

        Track track = Track.builder()
                .title(req.title())
                .genre(req.genre())
                .lengthSeconds(req.lengthSeconds())
                .artist(artist)
                .build();

        return trackRepo.save(track);
    }

    @Cacheable(value = "tracksByArtist", key = "#identifier")
    public List<TrackResponseDto> getTracksByArtist(String identifier) {
        Artist artist = artistRepo.findByExternalIdentifier(identifier)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found with identifier: " + identifier));
        return trackRepo.findAllByArtistId(artist.getId()).stream()
                .map(DtoMapper::toDto)
                .toList();
    }
}
