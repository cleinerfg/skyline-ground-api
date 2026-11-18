package com.skylineground.shared.startup;

import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.shared.config.uuid.UuidGenerator;
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

    @Override
    public void run(String... args) {

        if (userRepository.existsBySystemRole(SystemRole.ADMIN)) return;

        var admin = User.createAdmin(
                uuidGenerator.generate(),
                props.name(),
                props.email(),
                passwordEncoder.encode(props.password())
        );

        userRepository.save(admin);
    }
}
