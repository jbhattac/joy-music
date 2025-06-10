package com.music.joy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
/**
 * Factory class for retrieving the appropriate ArtistSelectionStrategy
 * based on a given time period.
 * This enables flexible and decoupled logic for selecting an artist
 * by delegating the responsibility to different strategy implementations.
 */
@Component
@RequiredArgsConstructor
public class ArtistStrategyFactory {

    private final Map<String, ArtistSelectionStrategy> strategies;

    public ArtistSelectionStrategy getStrategy(ArtistForPeriod period) {
        ArtistSelectionStrategy strategy = strategies.get(period.key());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown artist selection period: " + period);
        }
        return strategy;
    }
}
