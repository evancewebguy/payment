package com.kcb.payment.services.impl;

import com.kcb.payment.domain.Transaction;
import com.kcb.payment.dto.*;
import com.kcb.payment.exceptions.TransactionNotFoundException;
import com.kcb.payment.repositories.TransactionRepository;
import com.kcb.payment.services.PaymentProcessor;
import com.kcb.payment.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final PaymentProcessor paymentProcessor;

    @Override
    public BaseResponse<PaymentResponse> initiateB2CPayment(BaseRequest<PaymentInitiationRequest> paymentInitiationRequest) {
        PaymentResponse response = paymentProcessor.initiateB2CPayment(paymentInitiationRequest.getData());
        return BaseResponse.<PaymentResponse>builder()
                .status("Accepted for processing")
                .data(response)
                .build();
    }

    @Override
    public BaseResponse<Transaction> getPaymentStatus(BaseRequest<PaymentStatusRequest> paymentStatusRequest) {
        Transaction transaction = transactionRepository.findByPhoneNumberAndEndToEndId(paymentStatusRequest.getData().getPhone(), paymentStatusRequest.getData().getEndToEndId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with end to end id " + paymentStatusRequest.getData().getEndToEndId() + " and phone number " + paymentStatusRequest.getData().getPhone() + " not found"));

        return BaseResponse.<Transaction>builder()
                .status("Success")
                .data(transaction)
                .build();
    }

    @Override
    public BaseResponse<List<Transaction>> getAllTransactions() {
        return BaseResponse.<List<Transaction>>builder()
                .status("Success")
                .data(transactionRepository.findAll())
                .build();
    }

    @Override
    public BaseResponse<List<Transaction>> getUserTransactions(String phone) {
        return BaseResponse.<List<Transaction>>builder()
                .status("Success")
                .data(transactionRepository.findAllByPhoneNumber(phone))
                .build();
    }
}
