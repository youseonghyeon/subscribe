package com.example.subscribify.service.user;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.dto.UpdateUserDto;
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
     * 사용자 생성 (username, email 중복 검사)
     *
     * @param createUserDto
     * @return userId (PK : Long)
     */
    public Long createUser(CreateUserDto createUserDto) {

        if (isUsernameTaken(createUserDto.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        } else if (isEmailTaken(createUserDto.getEmail())) {
            throw new UserAlreadyExistsException("Email is already taken");
        }

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
     * @param userId 변경할 사용자의 PK
     * @param updateUserDto // TODO: 수정할 정보만 받아서 처리하도록 변경 (현재는 편의상 createUserDto로 받음)
     * @return User
     */
    @Transactional
    public Long updateUser(Long userId, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found userId : " + userId));
        user.update(
                passwordEncoder.encode(updateUserDto.getPassword()),
                updateUserDto.getEmail(),
                updateUserDto.getAddress(),
                updateUserDto.getCity(),
                updateUserDto.getState(),
                updateUserDto.getZip(),
                updateUserDto.getCountry());
        return userId;
    }

    /**
     * 사용자 삭제
     *
     * @param userId
     */
    public void deleteUser(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new NoSuchElementException("User not found userId : " + userId));
//        user.delete();
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
