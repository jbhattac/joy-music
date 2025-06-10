package com.music.joy.exception;

public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException() {
    }

    public ArtistNotFoundException(String message) {
        super(message);
    }
}
