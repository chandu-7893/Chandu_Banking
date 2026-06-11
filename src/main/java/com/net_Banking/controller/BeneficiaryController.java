package com.net_Banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.net_Banking.entity.Beneficiary;
import com.net_Banking.service.BeneficiaryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(
            BeneficiaryService beneficiaryService) {

        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping("/beneficiaries")
    public String beneficiaries(
            HttpSession session,
            Model model) {

        String username =
                (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/";
        }

        model.addAttribute(
                "beneficiary",
                new Beneficiary());

        model.addAttribute(
                "beneficiaries",
                beneficiaryService
                        .getBeneficiaries(username));

        return "beneficiary";
    }

    @PostMapping("/add-beneficiary")
    public String addBeneficiary(
            @ModelAttribute Beneficiary beneficiary,
            HttpSession session) {

        String username =
                (String) session.getAttribute("username");

        beneficiary.setUsername(username);

        beneficiaryService.addBeneficiary(
                beneficiary);

        return "redirect:/beneficiaries";
    }

    @GetMapping("/delete-beneficiary/{id}")
    public String deleteBeneficiary(
            @PathVariable Long id) {

        beneficiaryService.deleteBeneficiary(id);

        return "redirect:/beneficiaries";
    }
}