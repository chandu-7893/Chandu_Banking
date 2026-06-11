package com.net_Banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.net_Banking.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUsernameOrderByDateTimeDesc(String username);

    List<Transaction> findTop10ByOrderByDateTimeDesc();

    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.type='DEPOSIT'")
    Double getTotalDeposits();

    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.type='WITHDRAW'")
    Double getTotalWithdrawals();

    @Query("SELECT COALESCE(SUM(t.amount),0) FROM Transaction t WHERE t.type='TRANSFER SENT'")
    Double getTotalTransfers();
}