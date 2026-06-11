package com.net_Banking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaction {
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String username;
    private String type;
    private double amount;
    private String description;
    private LocalDateTime dateTime;
	public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getType() {
		return type;
	}
	public double getAmount() {
		return amount;
	}
	public String getDescription() {
		return description;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
    
}
