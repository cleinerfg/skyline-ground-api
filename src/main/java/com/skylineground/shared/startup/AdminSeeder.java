package com.skylineground.shared.startup;

import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.modules.identity.user.exception.InvalidPasswordException;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.modules.identity.user.validator.PasswordValidator;
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

        if (props.name() == null || props.name().isBlank())
            throw new IllegalStateException(
                    "Cannot initialize application: admin name is not configured."
            );

        if (props.email() == null || props.email().isBlank())
            throw new IllegalStateException(
                    "Cannot initialize application: admin email is not configured."
            );


        try {
            PasswordValidator.validate(props.password());
        } catch (InvalidPasswordException ex) {
            throw new IllegalStateException(
                    "Cannot initialize application: configured admin password is invalid.",
                    ex
            );
        }

        var admin = User.builder()
                .externalId(uuidGenerator.generate())
                .name(props.name())
                .email(props.email())
                .password(passwordEncoder.encode(props.password()))
                .build();

        admin.setSystemRole(SystemRole.ADMIN);

        userRepository.save(admin);
    }

}
