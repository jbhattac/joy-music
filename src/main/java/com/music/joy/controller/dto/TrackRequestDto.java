package com.music.joy.controller.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;



@Builder
public record TrackRequestDto (     @NotBlank(message = "Track name must not be blank")
                                    @Size(max = 100, message = "Track name must not exceed 100 characters") String title,
                                    @NotBlank(message = "Genre must not be blank") String genre,
                                    @NotNull(message = "Length of track is required")
                                    @Min(value = 30, message = "Track duration must be at least 30 seconds")
                                    @Max(value = 3600, message = "Track duration must not exceed 1 hour") int lengthSeconds){

}
