package com.skylineground.modules.identity.auth;

import com.skylineground.core.security.jwt.TokenService;
import com.skylineground.modules.identity.auth.dto.LoginRequest;
import com.skylineground.modules.identity.auth.dto.LoginResponse;
import com.skylineground.modules.identity.auth.exception.InvalidCredentialsException;
import com.skylineground.modules.identity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse authenticate(LoginRequest dto) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    dto.email(),
                    dto.password()
            );
            Authentication authentication = authenticationManager.authenticate(authToken);

            User user = (User) authentication.getPrincipal();
            String token = tokenService.generate(user);

            return new LoginResponse(token);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
    }
}
