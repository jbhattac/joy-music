package com.music.joy.service;

import com.music.joy.controller.dto.ArtistResponseDto;
import com.music.joy.exception.ArtistNotFoundException;
import com.music.joy.model.Artist;
import com.music.joy.repo.ArtistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static com.music.joy.controller.dto.DtoMapper.toDto;

/**
 * ArtistService Responsibilities
 * Managing artist data
 * Renaming artist
 * Getting artist of the day
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistService {
    private final ArtistRepository artistRepo;
    private final ArtistStrategyFactory strategyFactory;


    public Artist renameArtist(String identifier, String name) {
        Artist artist = artistRepo.findByExternalIdentifier(identifier)
                .orElseThrow(() -> new ArtistNotFoundException("No artists available"));
        artist.setName(name);
        artist.setExternalIdentifier(generateExternalIdentifier(name));
        return artistRepo.save(artist);
    }

    @Cacheable(value = "artistOfPeriod", key = "#period")
    public ArtistResponseDto getArtistOfPeriod(ArtistForPeriod period) {
        List<Artist> artists = artistRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Artist::getCreatedAt))
                .toList();

        Artist selected = strategyFactory.getStrategy(period).select(artists);

        return toDto(selected);
    }


    private String generateExternalIdentifier(String name) {

        StringBuilder identifier = new StringBuilder();
        boolean wasHyphen = false;

        for (char ch : name.toLowerCase().toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                identifier.append(ch);
                wasHyphen = false;
            } else if (!wasHyphen) {
                identifier.append('-');
                wasHyphen = true;
            }
        }

        int length = identifier.length();
        if (length > 0 && identifier.charAt(length - 1) == '-') {
            identifier.setLength(length - 1);
        }

        if (!identifier.isEmpty() && identifier.charAt(0) == '-') {
            identifier.deleteCharAt(0);
        }

        return identifier.toString();
    }
}
