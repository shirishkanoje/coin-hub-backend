package com.shirish.modal;

import com.shirish.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {

    @Id
    private String id;   // ✅ NO @GeneratedValue

    @ManyToOne
    private User user;

    private String otp;

    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    private String sendTo;
}