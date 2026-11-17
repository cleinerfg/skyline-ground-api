package com.skylineground.shared.config.uuid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UuidGeneratorTest {

    @Mock
    private Supplier<UUID> uuidV7Generator;

    @InjectMocks
    private UuidGenerator uuidGenerator;

    @Test
    void shouldReturnGeneratedUuid() {
        UUID expected = UUID.randomUUID();

        when(uuidV7Generator.get()).thenReturn(expected);

        UUID actual = uuidGenerator.generate();

        assertThat(actual).isEqualTo(expected);
        verify(uuidV7Generator).get();
    }
}
