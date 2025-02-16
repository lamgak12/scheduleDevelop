package com.example.scheduledevelop.global.filter;

public interface Const {
    String LOGIN_USER = "sessionKey"; // 세션 키
    String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,7}$";
    //"문자+숫자+특수문자가 포함된 아이디 + @ + 유효한 도메인명 + 최상위 도메인(2~7자)"
}

