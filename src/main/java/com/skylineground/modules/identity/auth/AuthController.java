package com.skylineground.modules.identity.auth;

import com.skylineground.modules.identity.auth.dto.LoginRequest;
import com.skylineground.modules.identity.auth.dto.LoginResponse;
import com.skylineground.modules.identity.auth.service.LoginService;
import com.skylineground.modules.identity.user.dto.UserRequest;
import com.skylineground.modules.identity.user.dto.UserResponse;
import com.skylineground.modules.identity.user.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CreateUserService createUserService;
    private final LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest dto) {
        UserResponse response = createUserService.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest dto) {
        LoginResponse response = loginService.authenticate(dto);
        return ResponseEntity.ok(response);
    }
}
