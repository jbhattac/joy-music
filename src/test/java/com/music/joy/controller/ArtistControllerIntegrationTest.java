package com.music.joy.controller;


import com.music.joy.controller.dto.TrackRequestDto;
import com.music.joy.controller.dto.TrackResponseDto;
import com.music.joy.model.Artist;
import com.music.joy.repo.ArtistRepository;
import com.music.joy.repo.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtistControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;


    private String baseUrl;

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.2"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/artists";
        trackRepository.deleteAll();
        artistRepository.deleteAll();

        artistRepository.save(Artist.builder().name("Taylor Swift").externalIdentifier("taylor-swift").build());
    }

    @Test
    void testAddTrackToArtist() {
        // Given
        String externalIdentifier = artistRepository.findAll().get(0).getExternalIdentifier();
        TrackRequestDto request = new TrackRequestDto("Blank Space", "Pop", 210);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TrackRequestDto> entity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<TrackResponseDto> response = restTemplate.exchange(
                baseUrl + "/" + externalIdentifier + "/tracks",
                HttpMethod.POST,
                entity,
                TrackResponseDto.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().title()).isEqualTo("Blank Space");
    }

    @Test
    void testGetArtistTracks() {
        // Given
        String externalIdentifier = artistRepository.findAll().get(0).getExternalIdentifier();
        TrackRequestDto request = new TrackRequestDto("Enchanted", "Pop", 100);

        restTemplate.postForEntity(baseUrl + "/" + externalIdentifier + "/tracks", request, TrackResponseDto.class);

        // When
        ResponseEntity<TrackResponseDto[]> response = restTemplate.getForEntity(
                baseUrl + "/" + externalIdentifier + "/tracks",
                TrackResponseDto[].class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    void testAddTrackToInvalidArtist_ShouldReturnNotFound() {
        // Given
        String invalidIdentifier = "non-existent-artist";
        TrackRequestDto request = new TrackRequestDto("Fake Song", "Rock", 200);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TrackRequestDto> entity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + invalidIdentifier + "/tracks",
                HttpMethod.POST,
                entity,
                String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Artist not found");
    }
}
