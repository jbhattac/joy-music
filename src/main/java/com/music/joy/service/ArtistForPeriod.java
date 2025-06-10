package com.music.joy.service;

public enum ArtistForPeriod {
    DAY("day");

    private final String key;

    ArtistForPeriod(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}
