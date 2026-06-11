package com.net_Banking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.net_Banking.entity.User;
import com.net_Banking.repository.TransactionRepository;
import com.net_Banking.repository.UserRepository;
import com.net_Banking.service.BankingService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class BankingController {

	private final BankingService bankingService;
	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;

	public BankingController(BankingService bankingService, TransactionRepository transactionRepository,
			UserRepository userRepository) {

		this.bankingService = bankingService;
		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user, Model model) {

		String result = bankingService.register(user);

		if (!result.equals("success")) {
			model.addAttribute("error", result);
			return "register";
		}

		model.addAttribute("success", "Account created successfully. Please login.");

		return "login";
	}
	@GetMapping("/login") public String loginPage() { return "login"; }

	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
			Model model) {

		User user = bankingService.login(username, password);

		if (user == null) {
			model.addAttribute("error", "Invalid username or password");
			return "login";
		}

		session.setAttribute("username", user.getUsername());

		if ("ADMIN".equalsIgnoreCase(user.getRole())) {
			return "redirect:/admin";
		}

		return "redirect:/dashboard";
	}

	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {

		String username = (String) session.getAttribute("username");

		if (username == null) {
			return "redirect:/";
		}

		User user = bankingService.getUser(username);

		model.addAttribute("user", user);

		return "dashboard";
	}

	@PostMapping("/deposit")
	public String deposit(@RequestParam double amount, HttpSession session) {

		String username = (String) session.getAttribute("username");

		bankingService.deposit(username, amount);

		return "redirect:/dashboard";
	}

	@PostMapping("/withdraw")
	public String withdraw(@RequestParam double amount, HttpSession session, Model model) {

		String username = (String) session.getAttribute("username");

		String result = bankingService.withdraw(username, amount);

		if (!result.equals("success")) {

			User user = bankingService.getUser(username);

			model.addAttribute("user", user);
			model.addAttribute("error", result);

			return "dashboard";
		}

		return "redirect:/dashboard";
	}

	@PostMapping("/transfer")
	public String transfer(@RequestParam String receiverAccountNumber, @RequestParam double amount, HttpSession session,
			Model model) {

		String username = (String) session.getAttribute("username");

		String result = bankingService.transfer(username, receiverAccountNumber, amount);

		if (!result.equals("success")) {

			User user = bankingService.getUser(username);

			model.addAttribute("user", user);
			model.addAttribute("error", result);

			return "dashboard";
		}

		return "redirect:/dashboard";
	}

	@GetMapping("/transactions")
	public String transactions(HttpSession session, Model model) {

		String username = (String) session.getAttribute("username");

		if (username == null) {
			return "redirect:/";
		}

		model.addAttribute("transactions", transactionRepository.findByUsernameOrderByDateTimeDesc(username));

		return "transactions";
	}

	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {

		String username = (String) session.getAttribute("username");

		if (username == null) {
			return "redirect:/";
		}

		User user = bankingService.getUser(username);

		model.addAttribute("user", user);

		return "profile";
	}

	@GetMapping("/edit-profile")
	public String editProfile(HttpSession session, Model model) {

		String username = (String) session.getAttribute("username");

		model.addAttribute("user", bankingService.getUser(username));

		return "edit-profile";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute User updatedUser, HttpSession session) {

		String username = (String) session.getAttribute("username");

		User existingUser = bankingService.getUser(username);

		existingUser.setFullName(updatedUser.getFullName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setPhone(updatedUser.getPhone());

		userRepository.save(existingUser);

		return "redirect:/profile";
	}

	@GetMapping("/statement")
	public void downloadStatement(HttpSession session, HttpServletResponse response) throws Exception {

		String username = (String) session.getAttribute("username");

		response.setContentType("application/pdf");

		response.setHeader("Content-Disposition", "attachment; filename=statement.pdf");

		Document document = new Document();

		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		document.add(new Paragraph("CHANDU BANK"));
		document.add(new Paragraph("----------------------"));
		document.add(new Paragraph("ACCOUNT STATEMENT"));
		document.add(new Paragraph("User : " + username));
		document.add(new Paragraph("Generated : " + java.time.LocalDateTime.now()));

		document.close();
	}

	@GetMapping("/change-password")
	public String changePasswordPage() {
		return "change-password";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			HttpSession session, Model model) {

		String username = (String) session.getAttribute("username");

		String result = bankingService.changePassword(username, oldPassword, newPassword);

		if (!result.equals("success")) {

			model.addAttribute("error", result);

			return "change-password";
		}

		model.addAttribute("success", "Password changed successfully");

		return "change-password";
	}
	
	@GetMapping("/card")
	public String card(HttpSession session, Model model) {

	    String username = (String) session.getAttribute("username");

	    System.out.println("Session Username = " + username);

	    if (username == null) {
	        return "redirect:/login";
	    }

	    User user = bankingService.getUser(username);

	    if (user == null) {
	        System.out.println("User not found in database");
	        return "redirect:/dashboard";
	    }

	    System.out.println("Card Number = " + user.getCardNumber());

	    model.addAttribute("user", user);

	    return "card";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/";
	}

}
