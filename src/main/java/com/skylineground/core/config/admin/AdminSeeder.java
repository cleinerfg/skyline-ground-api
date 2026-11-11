package com.skylineground.core.config.admin;

import com.skylineground.core.config.uuid.UuidGenerator;
import com.skylineground.modules.identity.user.User;
import com.skylineground.modules.identity.user.UserRepository;
import com.skylineground.modules.identity.user.constant.SystemRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final AdminProperties props;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UuidGenerator uuidGenerator;

    // Seed Admin when start Application
    @Override
    public void run(String... args) {
        if (!userRepository.existsBySystemRole(SystemRole.ADMIN)) {
            var admin = User.builder()
                    .externalId(uuidGenerator.generate())
                    .name(props.name())
                    .email(props.email())
                    .password(passwordEncoder.encode(props.password()))
                    .systemRole(SystemRole.ADMIN)
                    .build();

            userRepository.save(admin);
        }
    }
}
