package com.net_Banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.net_Banking.service.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService =
                notificationService;
    }

    @GetMapping("/notifications")
    public String notifications(
            HttpSession session,
            Model model) {

        String username =
                (String) session.getAttribute(
                        "username");

        if (username == null) {
            return "redirect:/";
        }

        model.addAttribute(
                "notifications",
                notificationService
                        .getNotifications(
                                username));

        return "notifications";
    }
}