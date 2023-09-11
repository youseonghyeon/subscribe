package com.example.subscribify.service.user;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.dto.UpdateUserDto;
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
        CreateUserDto createUserDto = createMockCreateUserDto();
        //when
        Long userId = userService.createUser(createUserDto);
        //then
        User createdUser = findUserByIdOrThrow(userId);
        assertUserEquals(createUserDto, createdUser);
    }

    @Test
    @Transactional
    @DisplayName("사용자 조회")
    void getUser() {
        //given
        CreateUserDto createUserDto = createMockCreateUserDto();
        User user = createMockUser(createUserDto);
        //when
        User createdUser = userService.getUser(user.getId());
        //then
        assertUserEquals(createUserDto, createdUser);
    }

    private User createMockUser(CreateUserDto createUserDto) {
        Long userId = userService.createUser(createUserDto);
        return findUserByIdOrThrow(userId);
    }

    @Test
    @Transactional
    @DisplayName("사용자 수정")
    void updateUser() {
        //given
        CreateUserDto createUserDto = createMockCreateUserDto();
        User user = createMockUser(createUserDto);
        UpdateUserDto updateUserDto = new UpdateUserDto("password2", "email2@gmail.com", "firstName2",
                "lastName2", "address2", "city2", "state2", "zip2", "country2");
        //when
        userService.updateUser(user.getId(), updateUserDto);
        //then
        User updatedUser = findUserByIdOrThrow(user.getId());
        assertUserEquals(convertToUpdate(createUserDto, updateUserDto), updatedUser);
    }

    @Test
    @Transactional
    @DisplayName("사용자 삭제")
    void deleteUser() {
        //given
        CreateUserDto createUserDto = createMockCreateUserDto();
        User user = createMockUser(createUserDto);
        //when
        assertThrows(UnsupportedOperationException.class,
                () -> userService.deleteUser(user.getId()));

    }

    private CreateUserDto createMockCreateUserDto() {
        return new CreateUserDto("username", "password", "password", "email", "firstName",
                "lastName", "address", "city", "state", "zip", "country");
    }

    private void assertUserEquals(CreateUserDto createUserDto, User user) {
        assertEquals(createUserDto.getUsername(), user.getUsername());
        assertTrue(passwordEncoder.matches(createUserDto.getPassword(), user.getPassword()));
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AssertionError("User should exist"));
    }

    private CreateUserDto convertToUpdate(CreateUserDto createUserDto, UpdateUserDto updateUserDto) {
        return new CreateUserDto(
                createUserDto.getUsername(),
                updateUserDto.getPassword(),
                updateUserDto.getPassword(),
                updateUserDto.getEmail(),
                updateUserDto.getFirstName(),
                updateUserDto.getLastName(),
                updateUserDto.getAddress(),
                updateUserDto.getCity(),
                updateUserDto.getState(),
                updateUserDto.getZip(),
                updateUserDto.getCountry());
    }
}
