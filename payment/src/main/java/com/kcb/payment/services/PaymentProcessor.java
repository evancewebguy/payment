package com.kcb.payment.services;

import com.kcb.payment.dto.PaymentInitiationRequest;
import com.kcb.payment.dto.PaymentResponse;


public interface PaymentProcessor {
    /**
     * Processes a B2C payment initiation request and returns the result.
     *
     * @param paymentInitiationRequest Details of the payment to process,
     *                                including amount, recipient, and payment channel
     * @return PaymentResponse containing transaction details and processing outcome
     */
    PaymentResponse initiateB2CPayment(PaymentInitiationRequest paymentInitiationRequest);

    /**
     * Processes a payment callback notification and returns the processing result.
     * This method handles asynchronous payment responses from payment gateways
     * or other payment processing systems.
     *
     * @param payload The payment callback notification containing transaction status
     * @return PaymentResponse with the processed callback result
     */
    PaymentResponse processPaymentCallback(PaymentResponse payload);
}