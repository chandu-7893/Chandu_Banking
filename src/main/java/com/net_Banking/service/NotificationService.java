package com.net_Banking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.net_Banking.entity.Notification;
import com.net_Banking.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    public void addNotification(
            String username,
            String title,
            String message) {

        Notification notification =
                new Notification();

        notification.setUsername(username);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setDateTime(
                LocalDateTime.now());

        notification.setRead(false);

        notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(
            String username) {

        return notificationRepository
                .findByUsernameOrderByDateTimeDesc(
                        username);
    }
}