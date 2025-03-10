package com.example.scheduledevelop.domain.schedule.repository;

import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
     Optional<Schedule> findScheduleById(Long id);
     Page<Schedule> findAll(Pageable pageable);
}
