package com.music.joy.controller.dto;


import lombok.Builder;



@Builder
public record TrackRequestDto ( String title,
        String genre,
        int lengthSeconds){

}
