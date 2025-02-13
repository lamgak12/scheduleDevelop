package com.example.scheduledevelop.domain.schedule.service;

import com.example.scheduledevelop.domain.schedule.dto.request.ScheduleCreateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.response.ScheduleResponseDto;
import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import com.example.scheduledevelop.domain.schedule.repository.ScheduleRepository;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.repository.UserRepository;
import com.example.scheduledevelop.global.PageResponseDto;
import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository; // 순환 참조 ex) 유저

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto dto, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");

        User user = userRepository.findUserById(sessionUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Schedule schedule = new Schedule(dto.getTitle(), dto.getContents(), user);
        scheduleRepository.save(schedule);
        return new ScheduleResponseDto(schedule);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ScheduleResponseDto> findAllSchedules(Pageable pageable) {
        Page<ScheduleResponseDto> schedulePage = scheduleRepository.findAll(pageable).map(ScheduleResponseDto::new);
        return new PageResponseDto<>(schedulePage);
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto findScheduleById(Long id){
        Schedule schedule = scheduleRepository.findScheduleById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
        return  new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto dto, HttpSession session){
        Long sessionUserId = (Long) session.getAttribute("userId");

        Schedule schedule = scheduleRepository.findScheduleById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getUser().getId().equals(sessionUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SCHEDULE_ACCESS);
        }
        // 제목이 있으면 변경
        if(dto.getTitle() != null && !dto.getTitle().trim().isEmpty()){
            schedule.updateTitle(dto.getTitle());
        }
        // 내용이 있으면 변경
        if (dto.getContents() != null && !dto.getContents().trim().isEmpty()) {
            schedule.updateContents(dto.getContents());
        }

        return new ScheduleResponseDto(schedule);
    }

    public void deleteSchedule(Long id, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");

        Schedule schedule = scheduleRepository.findScheduleById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getUser().getId().equals(sessionUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_SCHEDULE_ACCESS);
        }

        scheduleRepository.delete(schedule);
    }
}
