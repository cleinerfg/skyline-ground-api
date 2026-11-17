package com.skylineground.modules.identity.auth.web;

import com.skylineground.modules.identity.auth.dto.LoginRequest;
import com.skylineground.modules.identity.auth.dto.LoginResponse;
import com.skylineground.modules.identity.auth.service.LoginService;
import com.skylineground.modules.identity.auth.web.doc.LoginUserDoc;
import com.skylineground.modules.identity.auth.web.doc.RegisterUserDoc;
import com.skylineground.modules.identity.user.dto.CreateUserRequest;
import com.skylineground.modules.identity.user.dto.UserResponse;
import com.skylineground.modules.identity.user.service.CreateUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Authentication management endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CreateUserService createUserService;
    private final LoginService loginService;

    @RegisterUserDoc
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = createUserService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @LoginUserDoc
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = loginService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
