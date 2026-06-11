package com.net_Banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.net_Banking.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUsername(String username);

    List<Loan> findByStatus(String status);

}
