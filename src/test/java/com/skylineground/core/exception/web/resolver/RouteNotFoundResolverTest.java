package com.skylineground.core.exception.web.resolver;

import com.skylineground.core.exception.ErrorCode;
import com.skylineground.core.exception.route.RouteNotFoundException;
import com.skylineground.core.exception.web.support.ProblemDetailFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteNotFoundResolverTest {

    @Mock
    private ProblemDetailFactory problemDetailFactory;

    @InjectMocks
    private RouteNotFoundResolver resolver;

    private RouteNotFoundException exception;

    @BeforeEach
    void setUp() {
        exception = new RouteNotFoundException();
    }

    @Test
    void shouldResolveRouteNotFound() {
        ProblemDetail expected = ProblemDetail.forStatusAndDetail(
                ErrorCode.ROUTE_NOT_FOUND.getStatus(),
                exception.getMessage()
        );

        when(problemDetailFactory.create(
                exception.getErrorCode(),
                exception.getMessage()
        )).thenReturn(expected);

        assertThat(expected).isNotNull();

        ProblemDetail result = resolver.resolve();

        assertThat(result).isEqualTo(expected);

        verify(problemDetailFactory).create(
                exception.getErrorCode(),
                exception.getMessage()
        );
    }
}
