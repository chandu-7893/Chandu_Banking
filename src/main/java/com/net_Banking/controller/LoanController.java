package com.net_Banking.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.net_Banking.entity.Loan;
import com.net_Banking.repository.LoanRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoanController {

    private final LoanRepository loanRepository;

    public LoanController(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @GetMapping("/loan")
    public String loanPage() {
        return "loan";
    }

    @PostMapping("/apply-loan")
    public String applyLoan(
            @RequestParam double amount,
            @RequestParam int tenure,
            HttpSession session) {

        String username =
                (String) session.getAttribute("username");

        Loan loan = new Loan();

        loan.setUsername(username);
        loan.setAmount(amount);
        loan.setTenure(tenure);
        loan.setInterestRate(8.5);
        loan.setStatus("PENDING");

        loanRepository.save(loan);

        return "redirect:/my-loans";
    }

    @GetMapping("/my-loans")
    public String myLoans(
            HttpSession session,
            Model model) {

        String username =
                (String) session.getAttribute("username");

        List<Loan> loans =
                loanRepository.findByUsername(username);

        model.addAttribute("loans", loans);

        return "my-loans";
    }

    @GetMapping("/admin-loans")
    public String adminLoans(Model model) {

        model.addAttribute(
                "loans",
                loanRepository.findAll());

        return "admin-loans";
    }

    @GetMapping("/approve-loan/{id}")
    public String approveLoan(
            @PathVariable Long id) {

        Loan loan =
                loanRepository.findById(id).orElse(null);

        if (loan != null) {
            loan.setStatus("APPROVED");
            loanRepository.save(loan);
        }

        return "redirect:/admin-loans";
    }

    @GetMapping("/reject-loan/{id}")
    public String rejectLoan(
            @PathVariable Long id) {

        Loan loan =
                loanRepository.findById(id).orElse(null);

        if (loan != null) {
            loan.setStatus("REJECTED");
            loanRepository.save(loan);
        }

        return "redirect:/admin-loans";
    }
}