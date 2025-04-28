package com.kcb.payment.services.impl;

import com.kcb.payment.domain.Transaction;
import com.kcb.payment.domain.TransactionStatus;
import com.kcb.payment.dto.NotificationRequest;
import com.kcb.payment.dto.PaymentInitiationRequest;
import com.kcb.payment.dto.PaymentResponse;
import com.kcb.payment.dto.PaymentStatus;
import com.kcb.payment.exceptions.TransactionNotFoundException;
import com.kcb.payment.repositories.TransactionRepository;
import com.kcb.payment.services.NotificationService;
import com.kcb.payment.services.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Random;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MpesaPaymentProcessor implements PaymentProcessor {
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    private final Random random = new Random();

    @Override
    public PaymentResponse initiateB2CPayment(PaymentInitiationRequest paymentInitiationRequest) {
        //Save Transaction
        String endToEndId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction();
        transaction.setAmount(paymentInitiationRequest.getAmount());
        transaction.setProvider(paymentInitiationRequest.getProvider());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setPhoneNumber(paymentInitiationRequest.getPhoneNumber());
        transaction.setProvider(paymentInitiationRequest.getProvider());
        transaction.setEndToEndId(endToEndId);

        // Generate an initial transaction response
        PaymentResponse initialResponse = PaymentResponse.builder()
                .endToEndId(endToEndId)
                .status(PaymentStatus.INITIATED)
                .build();

        // Simulate the delayed callback asynchronously
        simulateCallback(initialResponse, paymentInitiationRequest);

        return initialResponse;
    }

    @Async
    public void simulateCallback(PaymentResponse initialResponse, PaymentInitiationRequest paymentInitiationRequest) {
        try {
            Thread.sleep(10_000); // Simulate 10-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        PaymentStatus finalStatus = random.nextBoolean() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        PaymentResponse finalResponse = PaymentResponse.builder()
                .endToEndId(initialResponse.getEndToEndId())
                .transactionId(initialResponse.getTransactionId())
                .status(finalStatus)
                .build();

        // Call the callback
        processPaymentCallback(finalResponse);

        CompletableFuture.completedFuture(null);
    }

    @Override
    public PaymentResponse processPaymentCallback(PaymentResponse payload) {
        Transaction transaction = transactionRepository.findByEndToEndId(payload.getEndToEndId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction Not Found"));
        NotificationRequest notification = new NotificationRequest();
        notification.setPhone(transaction.getPhoneNumber());
        if (payload.getStatus() == PaymentStatus.SUCCESS) {
            transaction.setStatus(TransactionStatus.SUCCESS);
            notification.setMessage("Confirmed the transaction was successful");
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            notification.setMessage("Transaction Failed");
        }
        transactionRepository.save(transaction);
        notificationService.sendNotification(notification);
        return payload;
    }
}
