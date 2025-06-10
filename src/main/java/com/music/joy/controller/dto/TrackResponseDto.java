package com.music.joy.controller.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record TrackResponseDto(String title,
        String genre,
        int lengthInSeconds) implements Serializable {
}
