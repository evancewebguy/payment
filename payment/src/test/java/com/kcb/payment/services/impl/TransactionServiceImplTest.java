package com.kcb.payment.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.kcb.payment.domain.Transaction;
import com.kcb.payment.domain.TransactionStatus;
import com.kcb.payment.dto.*;
import com.kcb.payment.exceptions.TransactionNotFoundException;
import com.kcb.payment.repositories.TransactionRepository;
import com.kcb.payment.services.PaymentProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentProcessor paymentProcessor;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private BaseRequest<PaymentInitiationRequest> paymentInitiationRequest;
    private BaseRequest<PaymentStatusRequest> paymentStatusRequest;
    private PaymentInitiationRequest paymentInitiationData;
    private PaymentStatusRequest paymentStatusData;
    private Transaction transaction;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize request objects
        paymentInitiationData = new PaymentInitiationRequest();
        paymentInitiationData.setEndToEndId("1234");
        paymentInitiationRequest = BaseRequest.<PaymentInitiationRequest>builder()
                .data(paymentInitiationData)
                .build();

        paymentStatusData = new PaymentStatusRequest();
        paymentStatusData.setPhone("123456789");
        paymentStatusData.setEndToEndId("1234");
        paymentStatusRequest = BaseRequest.<PaymentStatusRequest>builder()
                .data(paymentStatusData)
                .build();

        // Initialize payment response and transaction mock
        paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionId("tx12345");
        paymentResponse.setStatus(PaymentStatus.SUCCESS);

        transaction = new Transaction();
        transaction.setPhoneNumber("123456789");
        transaction.setEndToEndId("1234");
        transaction.setStatus(TransactionStatus.SUCCESS);
    }

    @Test
    void testInitiateB2CPayment() {
        // Arrange: Mock paymentProcessor to return a payment response
        when(paymentProcessor.initiateB2CPayment(paymentInitiationData)).thenReturn(paymentResponse);

        // Act: Call the method
        BaseResponse<PaymentResponse> response = transactionService.initiateB2CPayment(paymentInitiationRequest);

        // Assert: Verify response status and payment response
        assertEquals("Accepted for processing", response.getStatus());
        assertNotNull(response.getData());
        assertEquals("tx12345", response.getData().getTransactionId());
        verify(paymentProcessor).initiateB2CPayment(paymentInitiationData);
    }

    @Test
    void testGetPaymentStatus_Success() {
        // Arrange: Mock the repository to return a transaction
        when(transactionRepository.findByPhoneNumberAndEndToEndId(paymentStatusData.getPhone(), paymentStatusData.getEndToEndId()))
                .thenReturn(Optional.of(transaction));

        // Act: Call the method
        BaseResponse<Transaction> response = transactionService.getPaymentStatus(paymentStatusRequest);

        // Assert: Verify the response
        assertEquals("Success", response.getStatus());
        assertEquals(transaction, response.getData());
        verify(transactionRepository).findByPhoneNumberAndEndToEndId(paymentStatusData.getPhone(), paymentStatusData.getEndToEndId());
    }

    @Test
    void testGetPaymentStatus_TransactionNotFound() {
        // Arrange: Mock the repository to return an empty Optional
        when(transactionRepository.findByPhoneNumberAndEndToEndId(paymentStatusData.getPhone(), paymentStatusData.getEndToEndId()))
                .thenReturn(Optional.empty());

        // Act & Assert: Call the method and expect an exception
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.getPaymentStatus(paymentStatusRequest);
        });
    }

    @Test
    void testGetAllTransactions() {
        // Arrange: Mock the repository to return a list of transactions
        List<Transaction> transactions = Collections.singletonList(transaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act: Call the method
        BaseResponse<List<Transaction>> response = transactionService.getAllTransactions();

        // Assert: Verify the response
        assertEquals("Success", response.getStatus());
        assertEquals(transactions, response.getData());
        verify(transactionRepository).findAll();
    }

    @Test
    void testGetUserTransactions() {
        // Arrange: Mock the repository to return a list of transactions for a specific phone
        List<Transaction> transactions = Collections.singletonList(transaction);
        when(transactionRepository.findAllByPhoneNumber("123456789")).thenReturn(transactions);

        // Act: Call the method
        BaseResponse<List<Transaction>> response = transactionService.getUserTransactions("123456789");

        // Assert: Verify the response
        assertEquals("Success", response.getStatus());
        assertEquals(transactions, response.getData());
        verify(transactionRepository).findAllByPhoneNumber("123456789");
    }
}
