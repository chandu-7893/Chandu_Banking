package com.net_Banking.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.net_Banking.entity.Transaction;
import com.net_Banking.entity.User;
import com.net_Banking.repository.TransactionRepository;
import com.net_Banking.repository.UserRepository;
import java.util.Random;
@Service
public class BankingService {
	private final UserRepository userRepository;
	private final TransactionRepository transactionRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final NotificationService notificationService;

	public BankingService(
	        UserRepository userRepository,
	        TransactionRepository transactionRepository,
	        BCryptPasswordEncoder passwordEncoder,
	        NotificationService notificationService) {

	    this.userRepository = userRepository;
	    this.transactionRepository = transactionRepository;
	    this.passwordEncoder = passwordEncoder;
	    this.notificationService = notificationService;
	}

	public String register(User user) {

	    if (userRepository.existsByUsername(user.getUsername())) {
	        return "Username already exists";
	    }

	    user.setPassword(passwordEncoder.encode(user.getPassword()));

	    if (user.getRole() == null || user.getRole().isEmpty()) {
	        user.setRole("USER");
	    }

	    user.setAccountNumber(generateAccountNumber());

	    Random random = new Random();

	    user.setCardNumber(
	            "5245 " +
	            (1000 + random.nextInt(9000)) + " " +
	            (1000 + random.nextInt(9000)) + " " +
	            (1000 + random.nextInt(9000))
	    );

	    user.setExpiryDate("12/31");

	    user.setCvv(
	            String.valueOf(100 + random.nextInt(900))
	    );

	    user.setBalance(1000);

	    System.out.println("Card Number: " + user.getCardNumber());
	    System.out.println("Expiry: " + user.getExpiryDate());
	    System.out.println("CVV: " + user.getCvv());

	    userRepository.save(user);

	    saveTransaction(
	            user.getUsername(),
	            "OPENING BALANCE",
	            1000,
	            "Account created successfully"
	    );

	    return "success";
	}	
	
	public User login(String username, String password) {

		User user = userRepository.findByUsername(username);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {

			return user;
		}

		return null;
	}

	public User getUser(String username) {

	    if(username == null) {
	        return null;
	    }

	    return userRepository.findByUsername(username);
	}

	public void deposit(String username, double amount) {

	    User user = userRepository.findByUsername(username);

	    user.setBalance(user.getBalance() + amount);

	    userRepository.save(user);

	    saveTransaction(
	            username,
	            "DEPOSIT",
	            amount,
	            "Amount deposited");

	    notificationService.addNotification(
	            username,
	            "Deposit Successful",
	            "₹" + amount + " deposited successfully");
	}

	public String withdraw(String username, double amount) {

	    User user = userRepository.findByUsername(username);

	    if (user.getBalance() < amount) {
	        return "Insufficient balance";
	    }

	    user.setBalance(user.getBalance() - amount);

	    userRepository.save(user);

	    saveTransaction(
	            username,
	            "WITHDRAW",
	            amount,
	            "Amount withdrawn");

	    notificationService.addNotification(
	            username,
	            "Withdrawal Successful",
	            "₹" + amount + " withdrawn successfully");

	    return "success";
	}

	public String transfer(String senderUsername, String receiverAccountNumber, double amount) {

		User sender = userRepository.findByUsername(senderUsername);

		User receiver = userRepository.findByAccountNumber(receiverAccountNumber);

		if (receiver == null) {
			return "Receiver account not found";
		}

		if (sender.getBalance() < amount) {
			return "Insufficient balance";
		}

		sender.setBalance(sender.getBalance() - amount);

		receiver.setBalance(receiver.getBalance() + amount);

		userRepository.save(sender);
		userRepository.save(receiver);

		saveTransaction(sender.getUsername(), "TRANSFER SENT", amount,
				"Transferred to account : " + receiverAccountNumber);

		saveTransaction(receiver.getUsername(), "TRANSFER RECEIVED", amount,
				"Received from account : " + sender.getAccountNumber());

		notificationService.addNotification(sender.getUsername(), "Money Sent",
				"₹" + amount + " transferred to account " + receiverAccountNumber);

		notificationService.addNotification(receiver.getUsername(), "Money Received",
				"₹" + amount + " received from " + sender.getUsername());

		return "success";
	}

	public String changePassword(String username, String oldPassword, String newPassword) {

		User user = userRepository.findByUsername(username);

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {

			return "Current password incorrect";
		}

		user.setPassword(passwordEncoder.encode(newPassword));

		userRepository.save(user);

		return "success";
	}

	public void saveTransaction(String username, String type, double amount, String description) {

		Transaction transaction = new Transaction();

		transaction.setUsername(username);
		transaction.setType(type);
		transaction.setAmount(amount);
		transaction.setDescription(description);
		transaction.setDateTime(LocalDateTime.now());

		transactionRepository.save(transaction);
	}

	private String generateAccountNumber() {

		return "SBIN" + (100000000 + new Random().nextInt(900000000));
	}

}
