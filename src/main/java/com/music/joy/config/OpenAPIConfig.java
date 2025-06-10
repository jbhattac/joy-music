package com.music.joy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Music Metadata API", version = "v1"),
        servers = {
                @Server(url = "http://localhost:8080", description = "Server")
        }
)
@Configuration
public class OpenAPIConfig {
}
