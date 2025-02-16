package com.example.scheduledevelop.domain.user.service;

import com.example.scheduledevelop.domain.auth.dto.request.LoginRequestDto;
import com.example.scheduledevelop.domain.auth.dto.request.UserSignupRequestDto;
import com.example.scheduledevelop.domain.user.dto.request.UserUpdateRequestDto;
import com.example.scheduledevelop.domain.user.dto.response.UserResponseDto;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.repository.UserRepository;
import com.example.scheduledevelop.global.PageResponseDto;
import com.example.scheduledevelop.global.config.PasswordEncoder;
import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User login(LoginRequestDto requestDto) {

        // 이메일로 사용자가 존재하는지 확인
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INCORRECT_EMAIL));

        // 존재가 확인된 사용자의 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        return user;
    }

    @Transactional
    public UserResponseDto signupUser(UserSignupRequestDto dto) {

        // 다른 유저가 가입한 이메일인지 중복 검사
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
                });
        // 입력한 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 유저 생성
        User user = new User(dto.getUsername(), dto.getEmail(), encodedPassword);

        // 유저를 DB에 저장
        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<UserResponseDto> findAllUsers(Pageable pageable) {
        Page<UserResponseDto> userPage = userRepository.findAll(pageable).map(UserResponseDto::new);
        return new PageResponseDto<>(userPage);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long id) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(findUser);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto dto, Long sessionUserId){
        // 로그인 상태로 가정

        // db에 존재하는 유저인지 확인
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 접근 권한 확인 -> ex) 1번 유저의 변경 요청을 2번 유저가 시도한 경우
        if(!findUser.getId().equals(sessionUserId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 다른 유저가 사용하는 이메일인지 확인
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user1 -> {
                    throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
                });

        //이름, 이메일 값이 존재할 경우에만 업데이트
        if(dto.getUsername() != null && !dto.getUsername().trim().isEmpty()){
            findUser.updateUsername(dto.getUsername());
        }

        if(dto.getEmail() != null && !dto.getEmail().trim().isEmpty()){
            findUser.updateEmail(dto.getEmail());
        }

        return new UserResponseDto(findUser);
    }

    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword, Long sessionUserId) {
        // 로그인 상태로 가정
        // db에 존재하는 유저인지 확인
        User findUser = userRepository.findUserById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 접근 권한 확인 -> ex) 1번 유저의 변경 요청을 2번 유저가 시도한 경우
        if(!findUser.getId().equals(sessionUserId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(oldPassword, findUser.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        // 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 비밀번호 업데이트
        findUser.updatePassword(encodedPassword);
    }

    @Transactional
    public void deleteUser(Long id, String password, Long sessionUserId) {
        // 로그인 상태로 가정

        // db에 존재하는 유저인지 확인
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 접근 권한 확인 -> ex) 1번 유저의 변경 요청을 2번 유저가 시도한 경우
        if(!findUser.getId().equals(sessionUserId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        // 비밀번호 검증
        if(!passwordEncoder.matches(password, findUser.getPassword())){
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        // 유저 삭제
        userRepository.delete(findUser);
    }
}
