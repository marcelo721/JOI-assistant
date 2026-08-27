package com.marcelo271.neva_back_end.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GroqConfiguration {

    private String baseUrl = "https://api.groq.com/openai/v1";

    @Bean
    public WebClient groqWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
