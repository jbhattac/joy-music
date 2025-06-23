package com.music.joy.controller;

import com.music.joy.controller.dto.ArtistResponseDto;
import com.music.joy.controller.dto.DtoMapper;
import com.music.joy.controller.dto.TrackRequestDto;
import com.music.joy.controller.dto.TrackResponseDto;
import com.music.joy.model.Artist;
import com.music.joy.model.Track;
import com.music.joy.service.ArtistService;
import com.music.joy.service.TrackService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.music.joy.service.ArtistForPeriod.DAY;

@Tag(name = "Artist")
@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final TrackService trackService;


    @PostMapping("/{identifier}/tracks")
    public ResponseEntity<TrackResponseDto> addTrack(@Schema(example = "bon-jovi") @PathVariable String identifier, @RequestBody @Valid TrackRequestDto req) {
        final Track track = trackService.addTrack(identifier, req);
        return ResponseEntity.ok(DtoMapper.toDto(track));
    }

    @PutMapping("/{identifier}")
    public  ResponseEntity<ArtistResponseDto> renameArtist(@Schema(example = "bon-jovi") @PathVariable String identifier, @RequestBody String newName) {
        final Artist artist = artistService.renameArtist(identifier, newName);
        return ResponseEntity.ok(DtoMapper.toDto(artist));
    }

    @GetMapping("/{identifier}/tracks")
    public ResponseEntity<List<TrackResponseDto>> getTracks(@Schema(example = "bon-jovi") @PathVariable String identifier) {
        return ResponseEntity.ok(trackService.getTracksByArtist(identifier));
    }

    @GetMapping("/artist-of-the-day")
    public ResponseEntity<ArtistResponseDto> getArtistOfTheDay() {
        return ResponseEntity.ok(artistService.getArtistOfPeriod(DAY));
    }
}

