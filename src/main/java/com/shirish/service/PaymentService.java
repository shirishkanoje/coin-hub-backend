package com.shirish.service;

//import com.shirish.domain.PaymentMethod;
import com.razorpay.RazorpayException;
import com.shirish.domain.PaymentMethod;
import com.shirish.modal.PaymentOrder;
import com.shirish.modal.User;
import com.shirish.response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLing(User user, Long amount,Long orderId) throws RazorpayException;

    PaymentResponse createStripePaymentLing(User user, Long amount ,Long orderId ) throws StripeException;



}
