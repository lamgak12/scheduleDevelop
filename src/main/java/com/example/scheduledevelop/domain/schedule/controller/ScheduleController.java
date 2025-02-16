package com.example.scheduledevelop.domain.schedule.controller;

import com.example.scheduledevelop.domain.schedule.dto.request.ScheduleCreateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.response.ScheduleResponseDto;
import com.example.scheduledevelop.domain.schedule.service.ScheduleService;
import com.example.scheduledevelop.global.PageResponseDto;
import com.example.scheduledevelop.global.filter.Const;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @SessionAttribute(Const.LOGIN_USER) Long sessionUserId
    ){
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto, sessionUserId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ScheduleResponseDto>> findAllSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
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
            @Valid @RequestBody ScheduleUpdateRequestDto requestDto,
             @SessionAttribute(name = Const.LOGIN_USER) Long sessionUserId
    ) {

        ScheduleResponseDto responseDto = scheduleService.updateSchedule(id, requestDto, sessionUserId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteSchedule(
            @PathVariable Long id,
            @SessionAttribute(name = Const.LOGIN_USER) Long sessionUserId
    ){
        scheduleService.deleteSchedule(id, sessionUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
