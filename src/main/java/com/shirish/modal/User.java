package com.shirish.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shirish.domain.USER_ROLE;

import jakarta.persistence.Embedded;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ✅ Use IDENTITY for MySQL
    private Long id;

    private String fullName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();  // ✅ Corrected

    @Enumerated(EnumType.STRING)  // ✅ Save as String
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
}
