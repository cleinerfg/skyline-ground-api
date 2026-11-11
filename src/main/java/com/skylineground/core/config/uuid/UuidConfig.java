package com.skylineground.core.config.uuid;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Supplier;

@Configuration
public class UuidConfig {

    @Bean
    public Supplier<UUID> uuidV7Generator() {
        TimeBasedEpochGenerator generator = Generators.timeBasedEpochGenerator();
        return generator::generate;
    }
}
