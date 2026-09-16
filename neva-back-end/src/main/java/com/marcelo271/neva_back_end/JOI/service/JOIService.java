package com.marcelo271.neva_back_end.JOI.service;


import com.marcelo271.neva_back_end.JOI.entities.JOI;
import com.marcelo271.neva_back_end.JOI.repository.JOIRepository;
import com.marcelo271.neva_back_end.chat.service.UserChatService;
import com.marcelo271.neva_back_end.chat.service.DeviceCommandService;
import com.marcelo271.neva_back_end.chat.service.IntentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JOIService {

    private final JOIRepository joiRepository;

    public JOIService(JOIRepository joiRepository) {
        this.joiRepository = joiRepository;
    }

    @Transactional
    public void createNewJOI(JOI joi){
        joiRepository.save(joi);
    }

    @Transactional(readOnly = true)
    public JOI findById(Long id){
        return joiRepository.findById(id).orElseThrow(
                () -> new RuntimeException("entity not found")
        );
    }
}
