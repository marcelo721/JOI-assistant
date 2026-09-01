package com.marcelo271.neva_back_end.chat.service;


import com.marcelo271.neva_back_end.ai.Dto.GroqMessage;
import com.marcelo271.neva_back_end.ai.service.GroqService;
import com.marcelo271.neva_back_end.chat.enums.Intent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntentService {

    private final GroqService groqService;

    public IntentService(GroqService groqService) {
        this.groqService = groqService;
    }

    public Intent classify(String message){
        List<GroqMessage> messages = new ArrayList<>();
        messages.add(new GroqMessage("system",
                """
               Classifique a mensagem do usuário em apenas uma das categorias:

               CHAT:
               - perguntas
               - conversa
               - pedidos de informação
               - assuntos que não envolvem controlar dispositivos

               COMMAND:
               - ligar ou desligar dispositivos
               - alterar volume
               - alterar canal
               - alterar temperatura
               - controlar TV, ar condicionado, luz, ventilador, soundbar etc.

               Responda SOMENTE:
               CHAT
               ou
               COMMAND
               """
                ));

        messages.add(new GroqMessage("user", message));
        String response = groqService.chat(messages);
        return Intent.valueOf(response.trim().toUpperCase());
    }
}
