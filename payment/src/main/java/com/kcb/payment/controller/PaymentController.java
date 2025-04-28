package com.kcb.payment.controller;

import com.kcb.payment.dto.BaseRequest;
import com.kcb.payment.dto.BaseResponse;
import com.kcb.payment.dto.PaymentInitiationRequest;
import com.kcb.payment.dto.PaymentStatusRequest;
import com.kcb.payment.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<BaseResponse<?>> initiatePayment(
            @RequestBody BaseRequest<PaymentInitiationRequest> payload) {
        return ResponseEntity.accepted()
                .body(transactionService.initiateB2CPayment(payload));
    }

    @PostMapping("/status")
    public ResponseEntity<BaseResponse<?>> getPaymentStatus(
            @RequestBody BaseRequest<PaymentStatusRequest> payload) {
        return ResponseEntity.ok()
                .body(transactionService.getPaymentStatus(payload));
    }

    @GetMapping(params = "phone")
    public ResponseEntity<BaseResponse<?>> getUserPayments(
            @RequestParam("phone") String phone) {
        return ResponseEntity.ok()
                .body(transactionService.getUserTransactions(phone));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAllPayments() {
        return ResponseEntity.ok()
                .body(transactionService.getAllTransactions());
    }
}