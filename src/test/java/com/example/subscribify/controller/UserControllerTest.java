package com.example.subscribify.controller;

import com.example.subscribify.dto.controller.EnrollUserRequest;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.UserRepository;
import com.example.subscribify.util.SetupTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Import(SetupTestUtils.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SetupTestUtils setupTestUtils;

    @Test
    @DisplayName("로그인 폼 테스트")
    void loginForm() throws Exception {
        mockMvc.perform(get("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/login"));
    }

    @Test
    @DisplayName("로그인 성공 케이스")
    void loginSuccessCase() throws Exception {
        User user = setupTestUtils.createUser();
        String plainPassword = "testPassword";
        // when then
        performLogin(user.getUsername(), plainPassword)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("로그인 실패 케이스 - 비밀번호 불일치")
    void loginFailCase() throws Exception {
        User user = setupTestUtils.createUser();
        // when then
        performLogin(user.getUsername(), "wrongPassword")
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    @DisplayName("로그인 실패 케이스 - 빈값 존재")
    void loginFailCaseWithBlank() throws Exception {
        // when then
        performLogin("", "wrongPassword")
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
        // when then
        performLogin("", "")
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
        // when then
        performLogin("wrongUsername", "")
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    private ResultActions performLogin(String username, String password) throws Exception {
        return mockMvc.perform(post("/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password));
    }

    @Test
    @DisplayName("회원가입 성공 케이스")
    void signUpSuccessCase() throws Exception {
        //given
        String username = "tesetUser";
        EnrollUserRequest userDto = createMockUserDto(username);

        //when
        performSignUp(userDto)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        //then
        assertThat(userRepository.findByUsername(username)).isPresent();
    }

    @Test
    @DisplayName("회원가입 실패 케이스 - 중복된 Username")
    void 중복된_username_으로_회원가입_실패() throws Exception {
        //given
        String sameUsername = "sameUser";
        EnrollUserRequest firstUserDto = createMockUserDto(sameUsername, "first@mail.com");
        EnrollUserRequest secondUserDto = createMockUserDto(sameUsername, "second@mail.com");

        //when
        ResultActions firstSignUp = performSignUp(firstUserDto);
        ResultActions secondSignUp = performSignUp(secondUserDto);

        //then
        firstSignUp.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        secondSignUp.andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());

        long userCount = userRepository.countByUsername(sameUsername);
        assertThat(userCount).isEqualTo(1);
    }

    @Test
    @DisplayName("회원가입 실패 케이스 - 중복된 Email")
    void 중복된_email_로_회원가입_실패() throws Exception {
        //given
        String sameEmail = "sameEmail@mail.com";
        EnrollUserRequest firstUserDto = createMockUserDto("firstUser", sameEmail);
        EnrollUserRequest secondUserDto = createMockUserDto("secondUser", sameEmail);

        //when
        ResultActions firstSignUp = performSignUp(firstUserDto);
        ResultActions secondSignUp = performSignUp(secondUserDto);

        firstSignUp.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        secondSignUp.andExpect(status().is2xxSuccessful())
                .andExpect(model().hasErrors());

        long emailCount = userRepository.countByEmail(sameEmail);
        assertThat(emailCount).isEqualTo(1);
    }

    private ResultActions performSignUp(EnrollUserRequest userDto) throws Exception {
        return mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", userDto.getUsername())
                .param("password", userDto.getPassword())
                .param("passwordConfirm", userDto.getPasswordConfirm())
                .param("email", userDto.getEmail())
                .param("firstName", userDto.getFirstName())
                .param("lastName", userDto.getLastName())
                .param("address", userDto.getAddress())
                .param("city", userDto.getCity())
                .param("state", userDto.getState())
                .param("zip", userDto.getZip())
                .param("country", userDto.getCountry())
        );
    }


    private EnrollUserRequest createMockUserDto(String username) {
        return this.createMockUserDto(username, "testEmail@mail.com");
    }

    private EnrollUserRequest createMockUserDto(String username, String email) {
        return new EnrollUserRequest(
                username,
                "qwer1234",
                "qwer1234",
                email,
                "firstName",
                "lastName",
                "address",
                "city",
                "state",
                "zip",
                "country");
    }

}
