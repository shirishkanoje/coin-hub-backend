package com.shirish.service;

import com.shirish.domain.VerificationType;
import com.shirish.modal.ForgotPasswordToken;
import com.shirish.modal.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(
            User user, String id, String otp,
            VerificationType verificationType, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteTOken(ForgotPasswordToken token);
}
