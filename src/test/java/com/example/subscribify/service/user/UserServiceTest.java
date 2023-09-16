package com.example.subscribify.service.user;

import com.example.subscribify.dto.controller.EnrollUserRequest;
import com.example.subscribify.dto.service.UpdateUserServiceRequest;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @Transactional
    @DisplayName("사용자 생성")
    void createUserSuccessCase() {
        //given
        EnrollUserRequest enrollUserRequest = createMockCreateUserDto();
        //when
        Long userId = userService.createUser(enrollUserRequest);
        //then
        User createdUser = findUserByIdOrThrow(userId);
        assertUserEquals(enrollUserRequest, createdUser);
    }

    @Test
    @Transactional
    @DisplayName("사용자 조회")
    void getUser() {
        //given
        EnrollUserRequest enrollUserRequest = createMockCreateUserDto();
        User user = createMockUser(enrollUserRequest);
        //when
        User createdUser = userService.getUser(user.getId());
        //then
        assertUserEquals(enrollUserRequest, createdUser);
    }

    private User createMockUser(EnrollUserRequest enrollUserRequest) {
        Long userId = userService.createUser(enrollUserRequest);
        return findUserByIdOrThrow(userId);
    }

    @Test
    @Transactional
    @DisplayName("사용자 수정")
    void updateUser() {
        //given
        EnrollUserRequest enrollUserRequest = createMockCreateUserDto();
        User user = createMockUser(enrollUserRequest);
        UpdateUserServiceRequest updateUserServiceRequest = new UpdateUserServiceRequest("password2", "email2@gmail.com", "firstName2",
                "lastName2", "address2", "city2", "state2", "zip2", "country2");
        //when
        userService.updateUser(user.getId(), updateUserServiceRequest);
        //then
        User updatedUser = findUserByIdOrThrow(user.getId());
        assertUserEquals(convertToUpdate(enrollUserRequest, updateUserServiceRequest), updatedUser);
    }

    @Test
    @Transactional
    @DisplayName("사용자 삭제")
    void deleteUser() {
        //given
        EnrollUserRequest enrollUserRequest = createMockCreateUserDto();
        User user = createMockUser(enrollUserRequest);
        //when
        assertThrows(UnsupportedOperationException.class,
                () -> userService.deleteUser(user.getId()));

    }

    private EnrollUserRequest createMockCreateUserDto() {
        return new EnrollUserRequest("username", "password", "password", "email", "firstName",
                "lastName", "address", "city", "state", "zip", "country");
    }

    private void assertUserEquals(EnrollUserRequest enrollUserRequest, User user) {
        assertEquals(enrollUserRequest.getUsername(), user.getUsername());
        assertTrue(passwordEncoder.matches(enrollUserRequest.getPassword(), user.getPassword()));
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AssertionError("User should exist"));
    }

    private EnrollUserRequest convertToUpdate(EnrollUserRequest enrollUserRequest, UpdateUserServiceRequest updateUserServiceRequest) {
        return new EnrollUserRequest(
                enrollUserRequest.getUsername(),
                updateUserServiceRequest.getPassword(),
                updateUserServiceRequest.getPassword(),
                updateUserServiceRequest.getEmail(),
                updateUserServiceRequest.getFirstName(),
                updateUserServiceRequest.getLastName(),
                updateUserServiceRequest.getAddress(),
                updateUserServiceRequest.getCity(),
                updateUserServiceRequest.getState(),
                updateUserServiceRequest.getZip(),
                updateUserServiceRequest.getCountry());
    }
}
