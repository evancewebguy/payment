package com.kcb.payment.services;

import com.kcb.payment.domain.Transaction;
import com.kcb.payment.dto.*;

import java.util.List;

/**
 * Service interface for handling financial transactions, including payment initiation,
 * status tracking, and transaction management.
 * <p>
 * This interface provides methods for B2C payment operations and transaction history
 * retrieval, ensuring standardized interaction patterns for all implementations.
 */
public interface TransactionService {
    /**
     * Initiates a Business-to-Consumer (B2C) payment transaction.
     *
     * @param paymentInitiationRequest Contains the payment details including amount,
     *                                 recipient information, and payment channel
     * @return BaseResponse containing PaymentResponse with transaction details
     * or error information if the request fails
     */
    BaseResponse<PaymentResponse> initiateB2CPayment(
            BaseRequest<PaymentInitiationRequest> paymentInitiationRequest);

    /**
     * Retrieves the current status of a previously initiated payment transaction.
     *
     * @param paymentStatusRequest Contains the transaction identifier
     *                             and other required status check parameters
     * @return BaseResponse containing PaymentStatusResponse with current
     * transaction status or error information
     */
    BaseResponse<Transaction> getPaymentStatus(
            BaseRequest<PaymentStatusRequest> paymentStatusRequest);

    /**
     * Fetches a comprehensive list of all available transactions.
     *
     * @return BaseResponse containing a List of Transaction objects
     * or error information if the request fails
     */
    BaseResponse<List<Transaction>> getAllTransactions();

    /**
     * Retrieves a filtered list of transactions associated with a specific phone number.
     *
     * @param phone The phone number to filter transactions by
     * @return BaseResponse containing a single Transaction object
     * or error information if the request fails
     */
    BaseResponse<List<Transaction>> getUserTransactions(String phone);
}
