package com.example.scheduledevelop.domain.comment.service;

import com.example.scheduledevelop.domain.comment.dto.CommentCreateRequestDto;
import com.example.scheduledevelop.domain.comment.dto.CommentResponseDto;
import com.example.scheduledevelop.domain.comment.dto.CommentUpdateRequestDto;
import com.example.scheduledevelop.domain.comment.entity.Comment;
import com.example.scheduledevelop.domain.comment.repository.CommentRepository;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(Long scheduleId, CommentCreateRequestDto requestDto, HttpSession session) {

        Long sessionUserId = (Long) session.getAttribute("userId");

        User user = userRepository.findUserById(sessionUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정을 찾을 수 없습니다."));

        Comment comment = new Comment(schedule, user, requestDto.getContents());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllComment(Long scheduleId){
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 일정을 찾을 수 없습니다."));

        return commentRepository.findBySchedule(schedule).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto updateComment(Long scheduleId, Long commentId, CommentUpdateRequestDto requestDto, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."));

        // 댓글이 해당 일정에 속하는지 확인
        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 일정에 속하지 않는 댓글입니다.");
        }

        // 본인이 작성한 댓글인지 확인
        if (!comment.getUsers().getId().equals(sessionUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 작성한 댓글만 수정할 수 있습니다.");
        }

        // 댓글 내용 업데이트
        comment.updateContents(requestDto.getContents());

        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long scheduleId, Long commentId, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."));

        // 댓글이 해당 일정에 속하는지 확인
        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 일정에 속하지 않는 댓글입니다.");
        }

        // 본인이 작성한 댓글인지 확인
        if (!comment.getUsers().getId().equals(sessionUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}
