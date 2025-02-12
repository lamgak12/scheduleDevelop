package com.example.scheduledevelop.domain.comment.controller;

import com.example.scheduledevelop.domain.comment.dto.CommentCreateRequestDto;
import com.example.scheduledevelop.domain.comment.dto.CommentResponseDto;
import com.example.scheduledevelop.domain.comment.dto.CommentUpdateRequestDto;
import com.example.scheduledevelop.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{scheduleId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long scheduleId,
            @RequestBody CommentCreateRequestDto requestDto,
            HttpSession session
    ){
        CommentResponseDto responseDto = commentService.createComment(scheduleId, requestDto, session);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{scheduleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsBySchedule(@PathVariable Long scheduleId) {
        List<CommentResponseDto> comments = commentService.findAllComment(scheduleId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PatchMapping("/{scheduleId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDto requestDto,
            HttpSession session
    ) {
        commentService.updateComment(scheduleId, commentId, requestDto, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            HttpSession session
    ) {
        commentService.deleteComment(scheduleId, commentId, session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
