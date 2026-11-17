package com.skylineground.modules.identity.user.persistence;

import com.skylineground.modules.identity.user.constant.SystemRole;
import com.skylineground.shared.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "external_id", nullable = false, unique = true, updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID externalId;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "password_hash", nullable = false)
    private String password;

    @NotNull
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "system_role", nullable = false, columnDefinition = "system_role")
    private SystemRole systemRole = SystemRole.MEMBER;

    @Builder
    public User(UUID externalId, String name, String email, String password) {
        this.externalId = externalId;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
