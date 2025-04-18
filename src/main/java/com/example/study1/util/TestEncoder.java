package com.example.study1.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestEncoder {
    public static void main(String[] args) {
        String password = "1234";
        String encoded = new BCryptPasswordEncoder().encode(password);
        System.out.println("암호화된 비밀번호: " + encoded);
    }
}
