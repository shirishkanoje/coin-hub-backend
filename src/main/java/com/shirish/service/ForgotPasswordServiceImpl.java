package com.shirish.service;

import com.shirish.domain.VerificationType;
import com.shirish.modal.ForgotPasswordToken;
import com.shirish.modal.User;
import com.shirish.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository repository;

    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp,
                                           VerificationType verificationType, String sendTo) {

        ForgotPasswordToken token = new ForgotPasswordToken();

        token.setId(id);  // ✅ VERY IMPORTANT
        token.setUser(user);
        token.setOtp(otp);
        token.setVerificationType(verificationType);
        token.setSendTo(sendTo);

        return repository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(User user) {
        return repository.findByUserId(user.getId());
    }

    @Override
    public void deleteTOken(ForgotPasswordToken token) {
        repository.delete(token);
    }
}
