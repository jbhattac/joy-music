package com.music.joy.service;

import com.music.joy.controller.dto.ArtistResponseDto;
import com.music.joy.exception.ArtistNotFoundException;
import com.music.joy.model.Artist;
import com.music.joy.repo.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepo;

    @InjectMocks
    private ArtistService artistService;

    @Mock
    private ArtistStrategyFactory strategyFactory;

    @Mock
    private ArtistSelectionStrategy dailyStrategy;

    @Captor
    private ArgumentCaptor<Artist> artistCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void renameArtist_shouldUpdateNameAndIdentifierAndSave() {
        // Given
        String oldIdentifier = "bon-jovi";
        String newName = "Bon Jovi Rocks!";
        Artist existingArtist = new Artist();
        existingArtist.setId(1L);
        existingArtist.setName("Bon Jovi");
        existingArtist.setExternalIdentifier("bon-jovi");

        when(artistRepo.findByExternalIdentifier(oldIdentifier)).thenReturn(Optional.of(existingArtist));
        when(artistRepo.save(any(Artist.class))).thenAnswer(invocation -> (Artist) invocation.getArgument(0));


        // When
        Artist updated = artistService.renameArtist(oldIdentifier, newName);

        // Then
        verify(artistRepo).save(artistCaptor.capture());
        Artist savedArtist = artistCaptor.getValue();

        assertEquals(newName, savedArtist.getName());
        assertEquals("bon-jovi-rocks", savedArtist.getExternalIdentifier());
        assertEquals(updated, savedArtist);
    }

    @Test
    void renameArtist_shouldThrow_whenArtistNotFound() {
        // Given
        when(artistRepo.findByExternalIdentifier("unknown")).thenReturn(Optional.empty());

        // Expect
        assertThrows(ArtistNotFoundException.class, () ->
                artistService.renameArtist("unknown", "New Name")
        );
    }

    @Test
    void shouldReturnArtistOfTheDay_whenArtistsExist() {
        // Arrange
        Artist artist1 = new Artist();
        artist1.setName("Artist One");
        artist1.setCreatedAt(LocalDate.of(2020, 1, 1).atStartOfDay());

        Artist artist2 = new Artist();
        artist2.setName("Artist Two");
        artist2.setCreatedAt(LocalDate.of(2021, 1, 1).atStartOfDay());

        List<Artist> mockArtists = List.of(artist2, artist1); // out of order
        List<Artist> sortedArtists = mockArtists.stream()
                .sorted(Comparator.comparing(Artist::getCreatedAt))
                .toList();

        Artist expectedArtist = sortedArtists.get((int) (LocalDate.now(ZoneId.of("UTC")).toEpochDay() % sortedArtists.size()));

        when(artistRepo.findAll()).thenReturn(mockArtists);
        when(strategyFactory.getStrategy(ArtistForPeriod.DAY)).thenReturn(dailyStrategy);
        when(dailyStrategy.select(sortedArtists)).thenReturn(expectedArtist);

        // Act
        ArtistResponseDto result = artistService.getArtistOfPeriod(ArtistForPeriod.DAY);

        // Assert
        assertEquals(expectedArtist.getName(), result.name());
    }

}
