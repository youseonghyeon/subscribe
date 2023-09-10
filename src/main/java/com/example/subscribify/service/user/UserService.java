package com.example.subscribify.service.user;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.entity.User;
import com.example.subscribify.exception.UserAlreadyExistsException;
import com.example.subscribify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    /**
     * 사용자 생성
     *
     * @param createUserDto
     * @return userId (PK : Long)
     */
    public Long createUser(CreateUserDto createUserDto) {
        User user = User.builder()
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .email(createUserDto.getEmail())
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .address(createUserDto.getAddress())
                .city(createUserDto.getCity())
                .state(createUserDto.getState())
                .zip(createUserDto.getZip())
                .country(createUserDto.getCountry())
                .build();
        return userRepository.save(user).getId();
    }

    /**
     * 사용자 정보 조회
     *
     * @param userId
     * @return User
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found userId : " + userId));
    }

    /**
     * 사용자 정보 수정
     *
     * @param userId
     * @param createUserDto // TODO: 수정할 정보만 받아서 처리하도록 변경 (현재는 편의상 createUserDto로 받음)
     * @return User
     */
    @Transactional
    public Long updateUser(Long userId, CreateUserDto createUserDto) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found userId : " + userId));
        findUser.update(
                passwordEncoder.encode(createUserDto.getPassword()),
                createUserDto.getEmail(),
                createUserDto.getAddress(),
                createUserDto.getCity(),
                createUserDto.getState(),
                createUserDto.getZip(),
                createUserDto.getCountry());
        return userId;
    }

    /**
     * 사용자 삭제
     *
     * @param userId
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public boolean isUserUnique(CreateUserDto createUserDto) {
        return !isUsernameTaken(createUserDto.getUsername()) && !isEmailTaken(createUserDto.getEmail());
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
