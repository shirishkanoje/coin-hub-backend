package com.shirish.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shirish.domain.USER_ROLE;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")   // ✅ FIX (avoids PostgreSQL keyword issue)
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    @Enumerated(EnumType.STRING)
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
}
