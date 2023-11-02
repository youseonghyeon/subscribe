package com.example.subscribify.service.user;

import com.example.subscribify.dto.controller.EnrollUserRequest;
import com.example.subscribify.dto.service.UpdateUserServiceRequest;
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


    public Long createUser(EnrollUserRequest enrollUserRequest) {

        if (isUsernameTaken(enrollUserRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        } else if (isEmailTaken(enrollUserRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already taken");
        }

        User user = User.builder()
                .username(enrollUserRequest.getUsername())
                .password(passwordEncoder.encode(enrollUserRequest.getPassword()))
                .email(enrollUserRequest.getEmail())
                .firstName(enrollUserRequest.getFirstName())
                .lastName(enrollUserRequest.getLastName())
                .address(enrollUserRequest.getAddress())
                .city(enrollUserRequest.getCity())
                .state(enrollUserRequest.getState())
                .zip(enrollUserRequest.getZip())
                .country(enrollUserRequest.getCountry())
                .build();

        return userRepository.save(user).getId();
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found userId : " + userId));
    }

    @Transactional
    public Long updateUser(Long userId, UpdateUserServiceRequest updateUserServiceRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found userId : " + userId));
        user.update(
                passwordEncoder.encode(updateUserServiceRequest.getPassword()),
                updateUserServiceRequest.getEmail(),
                updateUserServiceRequest.getAddress(),
                updateUserServiceRequest.getCity(),
                updateUserServiceRequest.getState(),
                updateUserServiceRequest.getZip(),
                updateUserServiceRequest.getCountry());
        return userId;
    }

    public void deleteUser(Long userId) {
        throw new UnsupportedOperationException("Not supported yet");
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
