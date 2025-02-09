package com.example.scheduledevelop.domain.schedule.controller;

import com.example.scheduledevelop.domain.schedule.dto.ScheduleCreateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.ScheduleResponseDto;
import com.example.scheduledevelop.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> save(@RequestBody ScheduleCreateRequestDto requestDto){
        ScheduleResponseDto responseDto = scheduleService.save(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    private ResponseEntity<List<ScheduleResponseDto>> findAll(){
        List<ScheduleResponseDto> responseDtos = scheduleService.findAll();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
