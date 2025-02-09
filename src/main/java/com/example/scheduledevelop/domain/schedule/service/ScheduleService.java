package com.example.scheduledevelop.domain.schedule.service;

import com.example.scheduledevelop.domain.schedule.dto.ScheduleCreateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.ScheduleResponseDto;
import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import com.example.scheduledevelop.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDto save(ScheduleCreateRequestDto dto) {
        Schedule schedule = new Schedule(dto.getWriter(), dto.getTitle(), dto.getContents());
        Schedule savedschedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedschedule);
    }
}
