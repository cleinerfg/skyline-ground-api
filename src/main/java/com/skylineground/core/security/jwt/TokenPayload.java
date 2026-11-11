package com.skylineground.core.security.jwt;

import com.skylineground.modules.identity.user.constant.SystemRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record TokenPayload(
        UUID externalId,
        SystemRole systemRole
) {
}
