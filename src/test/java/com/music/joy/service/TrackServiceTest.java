package com.music.joy.service;

import com.music.joy.controller.dto.TrackRequestDto;
import com.music.joy.controller.dto.TrackResponseDto;
import com.music.joy.exception.ArtistNotFoundException;
import com.music.joy.model.Artist;
import com.music.joy.model.Track;
import com.music.joy.repo.ArtistRepository;
import com.music.joy.repo.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {
    @Mock
    private TrackRepository trackRepo;

    @Mock
    private ArtistRepository artistRepo;

    @InjectMocks
    private TrackService trackService;

    private Artist artist;
    private TrackRequestDto trackDto;
    private Track savedTrack;

    @BeforeEach
    void setUp() {

        artist = Artist.builder()
                .id(1L)
                .name("Bon Jovi")
                .externalIdentifier("bon-jovi")
                .build();

        trackDto = TrackRequestDto.builder()
                .title("It's My Life")
                .genre("Rock")
                .lengthSeconds(240)
                .build();

        savedTrack = Track.builder()
                .id(100L)
                .title("It's My Life")
                .genre("Rock")
                .lengthSeconds(240)
                .artist(artist)
                .build();
    }

    @Test
    void testAddTrack_shouldSaveAndReturnTrack() {
        // given
        when(artistRepo.findByExternalIdentifier("bon-jovi")).thenReturn(Optional.of(artist));
        when(trackRepo.save(any(Track.class))).thenReturn(savedTrack);

        // when
        Track result = trackService.addTrack("bon-jovi", trackDto);

        // then
        assertNotNull(result);
        assertEquals("It's My Life", result.getTitle());

        // Capture arguments passed to save()
        ArgumentCaptor<Track> trackCaptor = ArgumentCaptor.forClass(Track.class);
        verify(trackRepo).save(trackCaptor.capture());

        Track captured = trackCaptor.getValue();
        assertEquals("It's My Life", captured.getTitle());
        assertEquals("Rock", captured.getGenre());
        assertEquals(240, captured.getLengthSeconds());
        assertEquals(artist, captured.getArtist());
    }

    @Test
    void testAddTrack_shouldThrowWhenArtistNotFound() {
        // given
        when(artistRepo.findByExternalIdentifier("invalid-artist")).thenReturn(Optional.empty());

        // when + then
        assertThrows(ArtistNotFoundException.class, () ->
                trackService.addTrack("invalid-artist", trackDto));
    }

    @Test
    void testGetTracksByArtist_shouldReturnTracks() {
        // given
        List<Track> tracks = List.of(savedTrack);
        when(artistRepo.findByExternalIdentifier("bon-jovi")).thenReturn(Optional.of(artist));
        when(trackRepo.findAllByArtistId(1L)).thenReturn(tracks);

        // when
        List<TrackResponseDto> result = trackService.getTracksByArtist("bon-jovi");

        // then
        assertEquals(1, result.size());
        assertEquals("It's My Life", result.get(0).title());
    }

    @Test
    void testGetTracksByArtist_shouldThrowWhenArtistNotFound() {
        // given
        when(artistRepo.findByExternalIdentifier("missing")).thenReturn(Optional.empty());

        // when + then
        assertThrows(ArtistNotFoundException.class, () ->
                trackService.getTracksByArtist("missing"));
    }
}