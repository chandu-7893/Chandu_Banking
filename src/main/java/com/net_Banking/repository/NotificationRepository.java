package com.net_Banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.net_Banking.entity.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUsernameOrderByDateTimeDesc(
            String username);
}