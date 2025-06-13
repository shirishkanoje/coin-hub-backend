package com.shirish.modal;

import com.shirish.domain.VerificationType;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable  // ✅ Mark as embeddable for Hibernate
@Data
@NoArgsConstructor  //
public class TwoFactorAuth {
    private boolean enabled = false;
    private VerificationType sendTo;
}
