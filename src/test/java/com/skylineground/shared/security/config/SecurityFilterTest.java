package com.skylineground.shared.security.config;

import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.shared.security.jwt.TokenException;
import com.skylineground.shared.security.jwt.TokenPayload;
import com.skylineground.shared.security.jwt.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private SecurityFilter securityFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(tokenService);
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderDoesNotUseBearerScheme() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic some-token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        securityFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(tokenService);
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldSetAuthenticationWhenTokenIsValid() throws ServletException, IOException {
        String token = "valid-token";

        TokenPayload tokenPayload = TokenPayload.builder()
                .externalId(UUID.randomUUID())
                .systemRole(SystemRole.MEMBER)
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenService.validate(token)).thenReturn(tokenPayload);

        securityFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication)
                .isInstanceOf(UsernamePasswordAuthenticationToken.class);

        assertThat(authentication.getPrincipal()).isSameAs(tokenPayload);
        assertThat(authentication.getCredentials()).isNull();
        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_MEMBER");

        verify(tokenService).validate(token);
        verify(filterChain).doFilter(request, response);

        assertThat(request.getAttribute(SecurityFilter.AUTH_EXCEPTION_ATTRIBUTE)).isNull();
    }

    @Test
    void shouldClearSecurityContextAndStoreExceptionWhenTokenIsInvalid() throws ServletException, IOException {
        String token = "invalid-token";
        TokenException exception = mock(TokenException.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user", null)
        );

        when(tokenService.validate(token)).thenThrow(exception);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        assertThat(request.getAttribute(SecurityFilter.AUTH_EXCEPTION_ATTRIBUTE))
                .isSameAs(exception);

        verify(tokenService).validate(token);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldExtractTokenFromBearerAuthorizationHeader() throws ServletException, IOException {
        String token = "valid-token";

        TokenPayload tokenPayload = TokenPayload.builder()
                .externalId(UUID.randomUUID())
                .systemRole(SystemRole.ADMIN)
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenService.validate(token)).thenReturn(tokenPayload);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService).validate(token);
        verifyNoMoreInteractions(tokenService);
    }
}
