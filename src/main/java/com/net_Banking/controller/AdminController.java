package com.net_Banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.net_Banking.entity.User;
import com.net_Banking.repository.TransactionRepository;
import com.net_Banking.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AdminController(UserRepository userRepository,
                           TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session,
                                 Model model) {

        String username =
                (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/";
        }

        User user =
                userRepository.findByUsername(username);

        if (user == null ||
                !"ADMIN".equalsIgnoreCase(user.getRole())) {

            return "redirect:/dashboard";
        }

        model.addAttribute("totalUsers",
                userRepository.count());

        model.addAttribute("recentTransactions",
                transactionRepository.findAll());

        return "admin";
    }
}