package com.music.joy.service;

import com.music.joy.model.Artist;

import java.util.List;
/**
 * Strategy interface for selecting an Artist from a given list,
 * based on a specific time-based criteria.
 */
public interface ArtistSelectionStrategy {
    Artist select(List<Artist> artists);
}
