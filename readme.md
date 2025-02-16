# 일정 관리앱 - JPA
- Springboot와 Spring data JPA를 활용하여 일정 관리 앱을 구현
- Postman을 활용하여 요청 및 응답 확인
# 프로젝트 환경
###  - 언어: Java 17

### - 프레임워크: Spring Boot 3.4.2

### - 빌드 도구: Gradle

### - DB: MySQL

### - ORM: Spring Data JPA

### - 환경 변수 관리: dotenv-java

### - 비밀번호 암호화: Bcrypt
# 프로젝트 구조
<details>
<summary>프로젝트 구조</summary>

```
scheduleDevelop📁
│── .env 🌍
│── build.gradle 🛠️
└── src/main📁
        └── java/com/example/scheduledevelop📁
            │   └── resources📁
            │       └─── application.properties⚙️
            ├── ScheduleDevelopApplication☕
            ├── domain📁
            │   ├── auth📁
            │   │   ├── controller/AuthController☕
            │   │   ├── dto/request/LoginRequestDto☕
            │   │   ├── dto/request/UserSignupRequestDto☕
            │   ├── comment📁
            │   │   ├── controller/CommentController☕
            │   │   ├── dto/request/CommentCreateRequestDto☕
            │   │   ├── dto/request/CommentUpdateRequestDto☕
            │   │   ├── dto/response/CommentResponseDto☕
            │   │   ├── entity/Comment☕
            │   │   ├── repository/CommentRepository☕
            │   │   ├── service/CommentService☕
            │   ├── schedule📁
            │   │   ├── controller/ScheduleController☕
            │   │   ├── dto/request/ScheduleCreateRequestDto☕
            │   │   ├── dto/request/ScheduleUpdateRequestDto☕
            │   │   ├── dto/response/ScheduleResponseDto☕
            │   │   ├── entity/Schedule☕
            │   │   ├── repository/ScheduleRepository☕
            │   │   ├── service/ScheduleService☕
            │   ├── user📁
            │   │   ├── controller/UserController☕
            │   │   ├── dto/request/UserDeleteRequestDto☕
            │   │   ├── dto/request/UserPasswordUpdateRequestDto☕
            │   │   ├── dto/request/UserUpdateRequestDto☕
            │   │   ├── dto/response/UserResponseDto☕
            │   │   ├── entity/User☕
            │   │   ├── repository/UserRepository☕
            │   │   ├── service/UserService☕
            └── global📁
                ├── BaseEntity☕
                ├── PageResponseDto☕
                ├── config📁
                │   ├── FilterConfig☕
                │   ├── PasswordEncoder☕
                ├── exception📁
                │   ├── CustomException☕
                │   ├── ErrorCode☕
                │   ├── GlobalExceptionHandler☕
                │   ├── comment📁
                │   ├── schedule📁
                │   ├── user📁
                └─── filter/
                    ├── Const☕
                    └─── LoginFilter☕
        


```

</details>

# ERD & API 명세서
<details>
# ERD
<summary>ERD</summary>
![img.png](img.png)
</details>
<details>
<summary>API 명세서</summary>

# API 명세서

## 회원 (User)

## 1. 회원 가입 (회원 생성)

### URL: POST /users/signup

### 성공: 201 Created

### 실패:

### 400 Bad Request (필수 값, 길이, 패턴 검증 실패)

### 409 Conflict (중복 이메일)

### 요청 예시:
```json
{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "Password123!"
}
```
## 2. 로그인

### URL: POST /users/login

### 성공: 200 OK

### 실패:

### 401 Unauthorized (비밀번호 불일치)

### 404 Not Found (이메일 없음)

### 409 Conflict (중복 로그인)

### 요청 예시:
```json
{
    "email": "john@example.com",
    "password": "Password123!"
}
```

## 3. 로그아웃

### URL: POST /users/logout

### 성공: 200 OK

## 4. 회원 전체 조회

### URL: GET /users

### 성공: 200 OK

## 5. 회원 단건 조회

### URL: GET /users/{id}

### 성공: 200 OK

### 실패:  404 Not Found (회원 없음)

## 6. 회원 정보 수정

### URL: PATCH /users/{id}

### 성공: 200 OK

### 실패:

### 401 Unauthorized (비로그인)

### 403 Forbidden (권한 없음)

### 404 Not Found (회원 없음)

### 409 Conflict (중복 이메일)

### 요청 예시:
```json
{
    "username": "john_updated",
    "email": "john.new@example.com"
}
```

## 7. 회원 비밀번호 수정

### URL: PATCH /users/{id}/password

### 성공: 200 OK

### 실패:

### 401 Unauthorized (비로그인)

### 403 Forbidden (권한 없음)

### 404 Not Found (회원 없음)

### 요청 예시:
```json
{
    "oldPassword": "Password123!",
    "newPassword": "NewPassword456!"
}
```

### 8. 회원 삭제

### URL: DELETE /users/{id}

### 성공: 204 No Content

### 실패:

### 401 Unauthorized (비로그인)

### 404 Not Found (회원 없음)

### 요청 예시:
```json
{
    "password": "Password123!"
}
```

## 일정 (Schedule)

## 1. 일정 생성

### URL: POST /schedules

### 성공: 201 Created

### 실패:

### 401 Unauthorized (비로그인)

### 404 Not Found (유저 없음)

### 요청 예시:
```json
{
    "title": "Meeting with Team",
    "contents": "Discuss project timeline."
}
```

## 2. 일정 전체 조회

### URL: GET /schedules

### 성공: 200 OK

## 3. 일정 단건 조회

### URL: GET /schedules/{id}

### 성공: 200 OK

### 실패:

### 404 Not Found (일정 없음)

## 4. 일정 수정

### URL: PATCH /schedules/{id}

### 성공: 200 OK

### 실패:

### 400 Bad Request (내용 없음)

### 401 Unauthorized (비로그인)

### 403 Forbidden (권한 없음)

### 404 Not Found (일정 없음)

### 요청 예시:
```json
{
    "title": "Updated Meeting",
    "contents": "Discuss updated project details."
}
```

## 5. 일정 삭제

### URL: DELETE /schedules/{id}

### 성공: 204 No Content

### 실패:

### 401 Unauthorized (비로그인)

### 403 Forbidden (권한 없음)

### 404 Not Found (일정 없음)

## 댓글 (Comment)

## 1. 댓글 생성

### URL: POST /schedules/{scheduleId}/comments

### 성공: 201 Created

### 실패:

### 400 Bad Request (내용 없음)

### 401 Unauthorized (비로그인)

### 404 Not Found (일정 없음)

### 요청 예시:
```json
{
    "contents": "Great meeting today!"
}
```

## 2. 댓글 조회

### URL: GET /schedules/{scheduleId}/comments

### 성공: 200 OK

## 3. 댓글 수정

### URL: PATCH /schedules/{scheduleId}/comments/{commentId}

### 성공: 200 OK

### 실패:

### 400 Bad Request (내용 없음)

### 401 Unauthorized (비로그인)

### 403 Forbidden (권한 없음)

### 404 Not Found (일정 없음, 댓글 없음)

### 요청 예시:
```json
{
    "contents": "Updated comment text."
}
```

### 4. 댓글 삭제

### URL: DELETE /schedules/{scheduleId}/comments/{commentId}

### 성공: 204 No Content

### 실패:

### 401 Unauthorized (비로그인)

### 403 Forbidden (권한 없음)

### 404 Not Found (일정 없음, 댓글 없음)
</details>