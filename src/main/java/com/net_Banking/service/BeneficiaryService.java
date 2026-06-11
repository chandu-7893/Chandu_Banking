package com.net_Banking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.net_Banking.entity.Beneficiary;
import com.net_Banking.repository.BeneficiaryRepository;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(
            BeneficiaryRepository beneficiaryRepository) {

        this.beneficiaryRepository = beneficiaryRepository;
    }

    public void addBeneficiary(
            Beneficiary beneficiary) {

        beneficiaryRepository.save(beneficiary);
    }

    public List<Beneficiary> getBeneficiaries(
            String username) {

        return beneficiaryRepository.findByUsername(username);
    }

    public void deleteBeneficiary(Long id) {

        beneficiaryRepository.deleteById(id);
    }
}