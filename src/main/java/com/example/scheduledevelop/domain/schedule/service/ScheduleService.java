package com.example.scheduledevelop.domain.schedule.service;

import com.example.scheduledevelop.domain.schedule.dto.ScheduleCreateRequestDto;
import com.example.scheduledevelop.domain.schedule.dto.ScheduleResponseDto;
import com.example.scheduledevelop.domain.schedule.dto.ScheduleUpdateRequestDto;
import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import com.example.scheduledevelop.domain.schedule.repository.ScheduleRepository;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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
    private final UserRepository userRepository; // 순환 참조 ex) 유저

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto dto, HttpSession session) {
        // 세션에서 userId 가져오기
        Long sessionUserId = (Long) session.getAttribute("userId");

        User user = userRepository.findUserById(sessionUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        Schedule schedule = new Schedule(dto.getTitle(), dto.getContents(), user);
        scheduleRepository.save(schedule);
        return new ScheduleResponseDto(schedule);
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

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto dto, HttpSession session){
        Long sessionUserId = (Long) session.getAttribute("userId");

        Schedule schedule = scheduleRepository.findScheduleById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정을 찾을 수 없습니다."));

        if (!schedule.getUser().getId().equals(sessionUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 작성한 일정만 수정할 수 있습니다.");
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정을 찾을 수 없습니다."));

        if (!schedule.getUser().getId().equals(sessionUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 작성한 일정만 삭제할 수 있습니다.");
        }

        scheduleRepository.delete(schedule);
    }
}
