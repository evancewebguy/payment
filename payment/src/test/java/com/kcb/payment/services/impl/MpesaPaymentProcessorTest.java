package com.kcb.payment.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.kcb.payment.domain.Transaction;
import com.kcb.payment.domain.TransactionStatus;
import com.kcb.payment.dto.NotificationRequest;
import com.kcb.payment.dto.PaymentInitiationRequest;
import com.kcb.payment.dto.PaymentResponse;
import com.kcb.payment.dto.PaymentStatus;
import com.kcb.payment.exceptions.TransactionNotFoundException;
import com.kcb.payment.repositories.TransactionRepository;
import com.kcb.payment.services.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

class MpesaPaymentProcessorTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MpesaPaymentProcessor mpesaPaymentProcessor;

    private PaymentInitiationRequest paymentInitiationRequest;
    private PaymentResponse initialResponse;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize PaymentInitiationRequest and Transaction objects
        paymentInitiationRequest = new PaymentInitiationRequest();
        paymentInitiationRequest.setAmount(BigDecimal.valueOf(1000.0));
        paymentInitiationRequest.setPhoneNumber("1234567890");
        paymentInitiationRequest.setProvider("MPESA");

        transaction = new Transaction();
        transaction.setAmount(paymentInitiationRequest.getAmount());
        transaction.setPhoneNumber(paymentInitiationRequest.getPhoneNumber());
        transaction.setProvider(paymentInitiationRequest.getProvider());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setEndToEndId(UUID.randomUUID().toString());

        initialResponse = PaymentResponse.builder()
                .endToEndId(transaction.getEndToEndId())
                .status(PaymentStatus.INITIATED)
                .build();
    }


    @Test
    void testSimulateCallback_Success() throws InterruptedException {
        // Arrange: Simulate a successful payment response after 10 seconds
        PaymentResponse simulatedResponse = PaymentResponse.builder()
                .endToEndId(initialResponse.getEndToEndId())
                .transactionId(UUID.randomUUID().toString())
                .status(PaymentStatus.SUCCESS)
                .build();

        when(transactionRepository.findByEndToEndId(simulatedResponse.getEndToEndId()))
                .thenReturn(Optional.of(transaction));
        doNothing().when(notificationService).sendNotification(any(NotificationRequest.class));

        // Act: Simulate the callback (we'll just directly call the method for testing)
        mpesaPaymentProcessor.processPaymentCallback(simulatedResponse);

        // Assert: Verify that the transaction status is updated to SUCCESS and notification is sent
        assertEquals(TransactionStatus.SUCCESS, transaction.getStatus());
        verify(notificationService).sendNotification(any(NotificationRequest.class));  // Ensure notification is sent
        verify(transactionRepository).save(transaction);  // Ensure the transaction is saved
    }

    @Test
    void testSimulateCallback_Failure() throws InterruptedException {
        // Arrange: Simulate a failed payment response after 10 seconds
        PaymentResponse simulatedResponse = PaymentResponse.builder()
                .endToEndId(initialResponse.getEndToEndId())
                .transactionId(UUID.randomUUID().toString())
                .status(PaymentStatus.FAILED)
                .build();

        when(transactionRepository.findByEndToEndId(simulatedResponse.getEndToEndId()))
                .thenReturn(Optional.of(transaction));
        doNothing().when(notificationService).sendNotification(any(NotificationRequest.class));

        // Act: Simulate the callback (we'll just directly call the method for testing)
        mpesaPaymentProcessor.processPaymentCallback(simulatedResponse);

        // Assert: Verify that the transaction status is updated to FAILED and notification is sent
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        verify(notificationService).sendNotification(any(NotificationRequest.class));  // Ensure notification is sent
        verify(transactionRepository).save(transaction);  // Ensure the transaction is saved
    }

    @Test
    void testProcessPaymentCallback_TransactionNotFound() {
        // Arrange: Simulate the scenario where no transaction is found
        PaymentResponse simulatedResponse = PaymentResponse.builder()
                .endToEndId(UUID.randomUUID().toString())
                .status(PaymentStatus.FAILED)
                .build();

        when(transactionRepository.findByEndToEndId(simulatedResponse.getEndToEndId()))
                .thenReturn(Optional.empty());

        // Act & Assert: Expect a TransactionNotFoundException to be thrown
        assertThrows(TransactionNotFoundException.class, () -> {
            mpesaPaymentProcessor.processPaymentCallback(simulatedResponse);
        });
    }
}
