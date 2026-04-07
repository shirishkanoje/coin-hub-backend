package com.shirish.controller;

import com.razorpay.RazorpayException;
import com.shirish.domain.PaymentMethod;
import com.shirish.modal.PaymentOrder;
import com.shirish.modal.User;
import com.shirish.response.PaymentResponse;
import com.shirish.service.PaymentService;
import com.shirish.service.UserService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt) throws
            Exception,
            RazorpayException,
            StripeException {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentResponse paymentResponse;

        PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse = paymentService.createRazorpayPaymentLing(user, amount, order.getId());
        }
        else{
            paymentResponse = paymentService.createStripePaymentLing(user, amount, order.getId() );
        }

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);

    }

}


