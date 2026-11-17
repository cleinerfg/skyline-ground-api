package com.skylineground.modules.identity.user.service;

import com.skylineground.modules.identity.user.UserMapper;
import com.skylineground.modules.identity.user.dto.UserRequest;
import com.skylineground.modules.identity.user.dto.UserResponse;
import com.skylineground.modules.identity.user.exception.EmailAlreadyExistsException;
import com.skylineground.modules.identity.user.persistence.User;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import com.skylineground.modules.identity.user.validator.PasswordValidator;
import com.skylineground.shared.config.uuid.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UuidGenerator uuidGenerator;

    @Transactional
    public UserResponse execute(UserRequest request) {
        PasswordValidator.validate(request.password());

        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = mapper.toEntity(uuidGenerator.generate(), request);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = repository.save(user);

        return mapper.toResponse(savedUser);
    }
}
