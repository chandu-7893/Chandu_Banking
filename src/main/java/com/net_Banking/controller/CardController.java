package com.net_Banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.net_Banking.entity.User;
import com.net_Banking.service.BankingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CardController {

    private final BankingService bankingService;

    public CardController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/card")
    public String cardPage(
            HttpSession session,
            Model model) {

        String username =
                (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/";
        }

        User user =
                bankingService.getUser(username);

        model.addAttribute("user", user);

        return "card";
    }
}