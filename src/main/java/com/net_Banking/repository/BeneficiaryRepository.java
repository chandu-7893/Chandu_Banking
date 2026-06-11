package com.net_Banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.net_Banking.entity.Beneficiary;

public interface BeneficiaryRepository
        extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByUsername(String username);
}