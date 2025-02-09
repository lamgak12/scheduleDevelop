package com.example.scheduledevelop.domain.schedule.service;

import com.example.scheduledevelop.domain.schedule.dto.ScheduleCreateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.ScheduleResponseDto;
import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import com.example.scheduledevelop.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto dto) {
        Schedule schedule = new Schedule(dto.getWriter(), dto.getTitle(), dto.getContents());
        Schedule savedschedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedschedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto findScheduleById(Long id){
        Schedule schedule = scheduleRepository.findScheduleById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정을 찾을 수 없습니다."));
        return  new ScheduleResponseDto(schedule);
    }


}
