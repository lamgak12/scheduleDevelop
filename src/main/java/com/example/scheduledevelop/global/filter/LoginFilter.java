package com.example.scheduledevelop.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

public class LoginFilter implements Filter {

    private static final Set<String> EXCLUDED_PATHS = Set.of("/login", "/logout", "/signup");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        // Public API 예외처리
        if (isPublicAPI(requestURI, method)) {
            chain.doFilter(request, response);
            return;
        }

        // 세션 검사
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(Const.LOGIN_USER) == null) {
            sendUnauthorizedResponse(httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicAPI(String uri, String method) {
        return EXCLUDED_PATHS.contains(uri) || method.equals("GET");
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"로그인이 필요합니다.\"}");
    }
}
