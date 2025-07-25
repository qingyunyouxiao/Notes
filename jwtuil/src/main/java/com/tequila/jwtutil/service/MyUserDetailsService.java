package com.tequila.jwtutil.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tequila.jwtutil.config.JwtUtil;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return User.withUsername("testuser")
                .password("{noop}password")
                .authorities("USER")
                .build();
    }

    @RestController
    public class AuthController {
        private final JwtUtil jwtUtil;

        public AuthController(JwtUtil jwtUtil) {
            this.jwtUtil = jwtUtil;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        if (user.get("username").equals("testuser") && user.get("password").equals("password")) {
            String token = jwtUtil.generateToken(user.get("username"));
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RestController
    public class SampleController {

        @GetMapping("/hello")
        public String hello() {
            return "Hello, Authenticated User!";
        }
    }

}
