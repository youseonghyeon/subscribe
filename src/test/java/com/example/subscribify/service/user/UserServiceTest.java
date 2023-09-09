package com.example.subscribify.service.user;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.UserRepository;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    @DisplayName("사용자 생성 성공 케이스")
    void createUserSuccessCase() {
        //given
        CreateUserDto createUserDto = createMockCreateUserDto();
        Long userId = userService.createUser(createUserDto);
        //when
        User createdUser = userRepository.findById(userId).orElseThrow(() -> new AssertionError("User should exist"));
        //then
        equalUserTest(createUserDto, createdUser);
    }


    @Test
    @Transactional
    @DisplayName("사용자 조회 성공 케이스")
    void getUserSuccessCase() {
        //given
        CreateUserDto createUserDto = createMockCreateUserDto();
        Long userId = userService.createUser(createUserDto);
        //when
        User createdUser = userService.getUser(userId);
        //then
        equalUserTest(createUserDto, createdUser);
    }

    @Test
    @Transactional
    @DisplayName("사용자 수정 성공 케이스")
    void updateUserSuccessCase() {
        //given
        CreateUserDto createUserDto = createMockCreateUserDto();
        Long userId = userService.createUser(createUserDto);
        //when
        CreateUserDto updateDto = new CreateUserDto("username2", "password2", "password2", "email2", "firstName2",
                "lastName2", "address2", "city2", "state2", "zip2", "country2");
        userService.updateUser(userId, updateDto);
        //then
        User updatedUser = userRepository.findById(userId).orElseThrow(() -> new AssertionError("User should exist"));

        CreateUserDto expectedUpdatedUserInfo = new CreateUserDto("username", "password2", "password2", "email2", "firstName",
                "lastName", "address2", "city2", "state2", "zip2", "country2");
        assertEquals(userId, updatedUser.getId());
        equalUserTest(expectedUpdatedUserInfo, updatedUser);
    }

    @Test
    @Transactional
    @DisplayName("사용자 삭제 성공 케이스")
    void deleteUserSuccessCase() {
        //given
        CreateUserDto createUserDto = createMockCreateUserDto();
        Long userId = userService.createUser(createUserDto);
        //when
        userService.deleteUser(userId);
        //then
        Optional<User> deletedUser = userRepository.findById(userId);
        assertTrue(deletedUser.isEmpty());
    }

    private CreateUserDto createMockCreateUserDto() {
        return new CreateUserDto("username", "password", "password", "email", "firstName",
                "lastName", "address", "city", "state", "zip", "country");
    }

    private void equalUserTest(CreateUserDto createUserDto, User createdUser) {
        assertEquals(createUserDto.getUsername(), createdUser.getUsername());
        assertTrue(passwordEncoder.matches(createUserDto.getPassword(), createdUser.getPassword()));
        assertEquals(createUserDto.getEmail(), createdUser.getEmail());
        assertEquals(createUserDto.getFirstName(), createdUser.getFirstName());
        assertEquals(createUserDto.getLastName(), createdUser.getLastName());
        assertEquals(createUserDto.getAddress(), createdUser.getAddress());
        assertEquals(createUserDto.getCity(), createdUser.getCity());
        assertEquals(createUserDto.getState(), createdUser.getState());
        assertEquals(createUserDto.getZip(), createdUser.getZip());
        assertEquals(createUserDto.getCountry(), createdUser.getCountry());
    }

}
