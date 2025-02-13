package com.example.scheduledevelop.domain.schedule.controller;

import com.example.scheduledevelop.domain.schedule.dto.request.ScheduleCreateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.response.ScheduleResponseDto;
import com.example.scheduledevelop.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.scheduledevelop.domain.schedule.service.ScheduleService;
import com.example.scheduledevelop.global.PageResponseDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @Valid @RequestBody ScheduleCreateRequestDto requestDto,
            HttpSession session){
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, session);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ScheduleResponseDto>> findAllSchedules(
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC)Pageable pageable
            ){
        PageResponseDto<ScheduleResponseDto> schedules = scheduleService.findAllSchedules(pageable);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id){
        ScheduleResponseDto responseDto = scheduleService.findScheduleById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdateRequestDto dto,
            HttpSession session){
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(id, dto, session);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteSchedule(@PathVariable Long id, HttpSession session){
        scheduleService.deleteSchedule(id, session);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
