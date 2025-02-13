package com.example.scheduledevelop.global;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponseDto<T> {
    private List<T> contents;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private boolean isFirst;
    private boolean isLast;

    public PageResponseDto(Page<T> page) {
        this.contents = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.size = page.getSize();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
    }
}
