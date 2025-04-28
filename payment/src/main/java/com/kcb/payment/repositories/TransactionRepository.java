package com.kcb.payment.repositories;

import com.kcb.payment.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByPhoneNumber(String phoneNumber);

    Optional<Transaction> findByEndToEndId(String endToEndId);

    Optional<Transaction> findByPhoneNumberAndEndToEndId(String phoneNumber, String endToEndId);
}
