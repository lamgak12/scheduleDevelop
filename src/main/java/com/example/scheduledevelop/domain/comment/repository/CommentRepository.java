package com.example.scheduledevelop.domain.comment.repository;

import com.example.scheduledevelop.domain.comment.entity.Comment;
import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import com.example.scheduledevelop.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findBySchedule(Schedule schedule, Pageable pageable);
}
