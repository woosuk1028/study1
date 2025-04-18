package com.example.study1.service;

import com.example.study1.dto.UserDto;
import com.example.study1.entity.User;
import com.example.study1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserDto userDto) {
        if(userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 사용자입니다.");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setNickname(userDto.getNickname());
        user.setRole("ROLE_USER");
        user.setCreateDate(LocalDateTime.now());
        userRepository.save(user);
    }
}
