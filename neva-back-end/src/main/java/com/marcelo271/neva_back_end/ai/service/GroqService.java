package com.marcelo271.neva_back_end.ai.service;


import com.marcelo271.neva_back_end.ai.Dto.GroqChatRequest;
import com.marcelo271.neva_back_end.ai.Dto.GroqChatResponse;
import com.marcelo271.neva_back_end.ai.Dto.GroqMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GroqService {

    private final WebClient webClient;
    @Value("${groq.api.key}")
    private  String apiKey ;
    private String model = "openai/gpt-oss-20b";

    public GroqService(WebClient groqWebClient) {
        this.webClient = groqWebClient;
    }

    public String chat(List<GroqMessage> messages) {

        GroqChatRequest request =
                new GroqChatRequest(model, messages);

        GroqChatResponse response = webClient.post()
                .uri("/chat/completions")
                .header(
                        "Authorization",
                        "Bearer " + apiKey
                )
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GroqChatResponse.class)
                .block();

        return response.choices()
                .get(0)
                .message()
                .content();
    }
}
