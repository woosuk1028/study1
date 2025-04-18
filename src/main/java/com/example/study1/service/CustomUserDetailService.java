package com.example.study1.service;

import com.example.study1.entity.User;
import com.example.study1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("로그인 시도한 username: " + username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
        System.out.println("찾은 사용자: " + user.getUsername());
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}
