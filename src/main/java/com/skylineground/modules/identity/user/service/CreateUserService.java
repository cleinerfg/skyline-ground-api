package com.skylineground.modules.identity.user.service;

import com.skylineground.core.config.uuid.UuidGenerator;
import com.skylineground.modules.identity.user.UserMapper;
import com.skylineground.modules.identity.user.dto.UserRequest;
import com.skylineground.modules.identity.user.dto.UserResponse;
import com.skylineground.modules.identity.user.exception.EmailAlreadyExistsException;
import com.skylineground.modules.identity.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UuidGenerator uuidGenerator;

    @Transactional
    public UserResponse execute(UserRequest dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException();
        }

        var user = userMapper.toEntity(dto);
        user.setExternalId(uuidGenerator.generate());

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        var savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }
}
