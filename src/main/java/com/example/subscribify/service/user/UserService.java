package com.example.subscribify.service.user;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    /**
     * 사용자 생성
     * @param createUserDto
     * @return userId (PK : Long)
     */
    public Long createUser(CreateUserDto createUserDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * 사용자 정보 조회
     * @param userId
     * @return User
     */
    public User getUser(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * 사용자 정보 수정
     * @param userId
     * @param createUserDto // TODO: 수정할 정보만 받아서 처리하도록 변경
     * @return User
     */
    public User updateUser(Long userId, CreateUserDto createUserDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * 사용자 삭제
     * @param userId
     */
    public void deleteUser(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
