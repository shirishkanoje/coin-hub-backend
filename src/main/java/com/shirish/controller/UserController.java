package com.shirish.controller;

import com.shirish.domain.VerificationType;
import com.shirish.modal.ForgotPasswordToken;
import com.shirish.modal.User;
import com.shirish.modal.VerificationCode;
import com.shirish.request.ForgotPasswordTokenRequest;
import com.shirish.request.ResetPasswordRequest;
import com.shirish.response.ApiResponse;
import com.shirish.response.AuthResponse;
import com.shirish.service.EmailService;
import com.shirish.service.ForgotPasswordService;
import com.shirish.service.UserService;
import com.shirish.service.VerificationCodeService;
import com.shirish.utils.OtpUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    // ================= PROFILE =================
    @GetMapping("api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // ================= SEND VERIFICATION OTP =================
    @PostMapping("api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }

        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        verificationCodeService.deleteVerificationCodeById(verificationCode);

        return new ResponseEntity<>("Verification OTP Successfully Sent", HttpStatus.OK);
    }

    // ================= ENABLE 2FA =================
    @PatchMapping("api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)
                ? verificationCode.getEmail()
                : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            User updatedUser = userService.enableTwoFactorAuthentication(
                    verificationCode.getVerificationType(), sendTo, user);

            verificationCodeService.deleteVerificationCodeById(verificationCode);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

        throw new Exception("wrong otp");
    }

    // ================= FORGOT PASSWORD SEND OTP =================
    @PostMapping("auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @RequestBody ForgotPasswordTokenRequest req) throws Exception {

        User user = userService.findUserByEmail(req.getSendTo());

        String otp = OtpUtils.generateOTP();
        String id = UUID.randomUUID().toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user);

        if (token == null) {
            token = forgotPasswordService.createToken(
                    user, id, otp, req.getVerificationType(), req.getSendTo());
        }

        if (req.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ================= VERIFY OTP + RESET PASSWORD =================
    @PostMapping("auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req) throws Exception {

        ForgotPasswordToken token = forgotPasswordService.findById(id);

        if (token == null) {
            throw new Exception("Invalid session");
        }

        boolean isVerified = token.getOtp().equals(req.getOtp());

        if (isVerified) {
            userService.updatePassword(token.getUser(), req.getPassword());

            ApiResponse res = new ApiResponse();
            res.setMessage("Password updated Successfully");

            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }

        throw new Exception("wrong OTP");
    }
}