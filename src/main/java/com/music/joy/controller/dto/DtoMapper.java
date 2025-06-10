package com.music.joy.controller.dto;

import com.music.joy.model.Artist;
import com.music.joy.model.Track;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DtoMapper {

    public static ArtistResponseDto toDto(Artist artist) {
        List<TrackResponseDto> trackDtos = Optional.ofNullable(artist.getTracks())
                .orElse(Collections.emptyList())
                .stream()
                .map(DtoMapper::toDto)
                .collect(Collectors.toList());

        return ArtistResponseDto.builder()
                .name(artist.getName())
                .createdAt(artist.getCreatedAt())
                .tracks(trackDtos)
                .build();
    }

    public static TrackResponseDto toDto(Track track) {
        return TrackResponseDto.builder()
                .title(track.getTitle())
                .genre(track.getGenre())
                .lengthInSeconds(track.getLengthSeconds())
                .build();
    }
}
