package com.marcelo271.neva_back_end.ai;

import com.marcelo271.neva_back_end.ai.Dto.GroqMessage;
import com.marcelo271.neva_back_end.ai.service.GroqService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class AiTestController {

    private final GroqService groqService;

    public AiTestController(GroqService groqService) {
        this.groqService = groqService;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {

        List<GroqMessage> messages = List.of(
                new GroqMessage(
                        "system",
                        "Você é a Neva, uma assistente pessoal amigável. Responda em ingles."
                ),
                new GroqMessage(
                        "user",
                        message
                )
        );

        return groqService.chat(messages);
    }


}
