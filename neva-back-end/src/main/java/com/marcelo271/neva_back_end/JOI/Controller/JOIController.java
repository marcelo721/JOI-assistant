package com.marcelo271.neva_back_end.JOI.Controller;


import com.marcelo271.neva_back_end.JOI.Controller.dto.JOICreateDto;
import com.marcelo271.neva_back_end.JOI.Controller.dto.JOIResponseDto;
import com.marcelo271.neva_back_end.JOI.entities.JOI;
import com.marcelo271.neva_back_end.JOI.service.JOIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/JOI")
public class JOIController {

    private final JOIService joiService;

    public JOIController(JOIService joiService) {
        this.joiService = joiService;
    }

    @PostMapping
    public ResponseEntity<JOIResponseDto> createJOI(@RequestBody JOICreateDto dto) {
        JOI joi = JOICreateDto.ToEntity(dto);
        joiService.createNewJOI(joi);
        return ResponseEntity.ok(JOIResponseDto.toResponse(joi));
    }

}
