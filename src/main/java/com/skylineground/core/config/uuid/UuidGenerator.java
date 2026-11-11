package com.skylineground.core.config.uuid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class UuidGenerator {

    private final Supplier<UUID> uuidV7Generator;

    public UUID generate() {
        return uuidV7Generator.get();
    }
}
