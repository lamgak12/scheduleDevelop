package com.example.scheduledevelop.domain.comment.service;

import com.example.scheduledevelop.domain.comment.dto.request.CommentCreateRequestDto;
import com.example.scheduledevelop.domain.comment.dto.request.CommentUpdateRequestDto;
import com.example.scheduledevelop.domain.comment.dto.response.CommentResponseDto;
import com.example.scheduledevelop.domain.comment.entity.Comment;
import com.example.scheduledevelop.domain.comment.repository.CommentRepository;
import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import com.example.scheduledevelop.domain.schedule.repository.ScheduleRepository;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.repository.UserRepository;
import com.example.scheduledevelop.global.PageResponseDto;
import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(Long scheduleId, CommentCreateRequestDto requestDto, Long sessionUserId) {

        User user = userRepository.findUserById(sessionUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findScheduleById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        Comment comment = new Comment(schedule, user, requestDto.getContents());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<CommentResponseDto> findAllComments(Long scheduleId, Pageable pageable){

        Schedule schedule = scheduleRepository.findScheduleById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        Page<CommentResponseDto> commentPage = commentRepository
                .findBySchedule(schedule, pageable).map(CommentResponseDto::new);

        return new PageResponseDto<>(commentPage);
    }

    @Transactional
    public CommentResponseDto updateComment(
            Long scheduleId,
            Long commentId,
            CommentUpdateRequestDto requestDto,
            Long sessionUserId) {


        // 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getSchedule().getId().equals(scheduleId)){
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        if (!comment.getUser().getId().equals(sessionUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        // 댓글 내용 업데이트
        comment.updateContents(requestDto.getContents());

        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(
            Long scheduleId,
            Long commentId,
            Long sessionUserId) {

        // 댓글 존재 여부 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(!comment.getSchedule().getId().equals(scheduleId)){
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        if (!comment.getUser().getId().equals(sessionUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        commentRepository.delete(comment);
    }
}
