package com.example.scheduledevelop.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateRequestDto {
    @NotBlank(message = "댓글 내용은 필수 입력값 입니다.")
    @Size(max = 200, message = "댓글 내용은 200자까지 작성할 수 있습니다.")
    private final String contents;
}
