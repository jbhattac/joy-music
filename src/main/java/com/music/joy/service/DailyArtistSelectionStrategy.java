package com.music.joy.service;

import com.music.joy.exception.ArtistNotFoundException;
import com.music.joy.model.Artist;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
/**
 * Strategy implementation for selecting the "Artist of the Day".
 * Chooses an artist from the list based on the current UTC day.
 * The index is calculated using the day of the month modulo the size of the artist list.
 * This class is registered as a Spring component with the name "day"
 * and is used by the ArtistStrategyFactory.
 */
@Component("day")
public class DailyArtistSelectionStrategy implements ArtistSelectionStrategy {
    @Override
    public Artist select(List<Artist> artists) {
        if (artists.isEmpty()) throw new ArtistNotFoundException("No artists available");
        int index = (LocalDate.now(ZoneId.of("UTC")).getDayOfMonth() % artists.size());
        return artists.get(index);
    }
}
