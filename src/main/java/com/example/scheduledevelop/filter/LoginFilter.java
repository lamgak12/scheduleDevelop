package com.example.scheduledevelop.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class LoginFilter implements Filter {

    private static final String[] EXCLUDED_PATHS = {"/","/login", "/logout"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // 로그인, 회원가입 요청은 필터 예외 처리
        for (String path : EXCLUDED_PATHS) {
            if (requestURI.startsWith(path)) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요합니다.");
            return;
        }

        chain.doFilter(request, response);
    }
}
