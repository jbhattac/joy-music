package com.music.joy.service;

import com.music.joy.exception.ArtistNotFoundException;
import com.music.joy.model.Artist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DailyArtistSelectionStrategyTest {

    private DailyArtistSelectionStrategy strategy;

    private Clock fixedClock;

    private LocalDate fixedDate;

    @BeforeEach
    void setUp() {
        fixedDate = LocalDate.of(2025, 6, 9);
        fixedClock = Clock.fixed(fixedDate.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
        strategy = new DailyArtistSelectionStrategy(fixedClock);
    }

    @Test
    public void shouldThrowExceptionWhenArtistListIsEmpty() {
        List<Artist> emptyList = List.of();
        assertThrows(ArtistNotFoundException.class, () -> strategy.select(emptyList));
    }

    @Test
    void shouldReturnCorrectArtistBasedOnUtcDayOfMonth() {
        // Arrange a fixed UTC date (9th of the month)


        DailyArtistSelectionStrategy strategy = new DailyArtistSelectionStrategy(fixedClock);

        List<Artist> artists = List.of(
                Artist.builder().externalIdentifier("1").name("Artist A").build(),
                Artist.builder().externalIdentifier("2").name("Artist B").build(),
                Artist.builder().externalIdentifier("3").name("Artist C").build()
        );

        int expectedIndex = fixedDate.getDayOfMonth() % artists.size();
        Artist expectedArtist = artists.get(expectedIndex);

        // Act
        Artist result = strategy.select(artists);

        // Assert
        assertEquals(expectedArtist.getName(), result.getName());
    }

}