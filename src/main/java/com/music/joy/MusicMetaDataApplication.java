package com.music.joy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MusicMetaDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicMetaDataApplication.class, args);
    }

}
