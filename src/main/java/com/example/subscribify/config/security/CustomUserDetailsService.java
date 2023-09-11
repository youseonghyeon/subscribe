package com.example.subscribify.config.security;

import com.example.subscribify.config.security.CustomUserDetails;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * username이라는 값을 통해 로그인 및 인증 처리를 하므로 username 이라는 값은 불변이어야 함
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " is not found"));
        return new CustomUserDetails(findUser);
    }
}
