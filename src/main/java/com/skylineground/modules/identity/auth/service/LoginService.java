package com.skylineground.modules.identity.auth.service;

import com.skylineground.modules.identity.auth.dto.LoginRequest;
import com.skylineground.modules.identity.auth.dto.LoginResponse;
import com.skylineground.modules.identity.auth.exception.InvalidCredentialsException;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.shared.security.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenService.generate(user);
        return new LoginResponse(token);
    }
}
