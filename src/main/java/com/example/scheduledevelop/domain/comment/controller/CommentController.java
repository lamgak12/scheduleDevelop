package com.example.scheduledevelop.domain.comment.controller;

import com.example.scheduledevelop.domain.comment.dto.request.CommentCreateRequestDto;
import com.example.scheduledevelop.domain.comment.dto.request.CommentUpdateRequestDto;
import com.example.scheduledevelop.domain.comment.dto.response.CommentResponseDto;
import com.example.scheduledevelop.domain.comment.service.CommentService;
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
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{scheduleId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            @SessionAttribute(Const.LOGIN_USER) Long sessionUserId
    ){
        CommentResponseDto responseDto = commentService.createComment(scheduleId, requestDto, sessionUserId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{scheduleId}/comments")
    public ResponseEntity<PageResponseDto<CommentResponseDto>> getCommentsBySchedule(
            @PathVariable Long scheduleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt"));
        PageResponseDto<CommentResponseDto> comments = commentService.findAllComments(scheduleId, pageable);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PatchMapping("/{scheduleId}/comments/{commentId}")//수정필요
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            @SessionAttribute(Const.LOGIN_USER) Long sessionUserId
    ) {
        CommentResponseDto responseDto = commentService.updateComment(scheduleId, commentId, requestDto, sessionUserId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @SessionAttribute(Const.LOGIN_USER) Long sessionUserId
    ) {
        commentService.deleteComment(scheduleId, commentId, sessionUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
