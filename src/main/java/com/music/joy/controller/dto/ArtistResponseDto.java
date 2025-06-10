package com.music.joy.controller.dto;


import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ArtistResponseDto(String name,
        LocalDateTime createdAt,
        List<TrackResponseDto> tracks) implements Serializable {

}
