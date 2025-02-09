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
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleCreateRequestDto requestDto){
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules(){
        List<ScheduleResponseDto> schedules = scheduleService.findAllSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id){
        ScheduleResponseDto responseDto = scheduleService.findScheduleById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
