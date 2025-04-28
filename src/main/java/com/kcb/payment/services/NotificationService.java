package com.kcb.payment.services;

import com.kcb.payment.dto.NotificationRequest;
import com.kcb.payment.exceptions.NotificationException;


public interface NotificationService {
    /**
     * Sends a notification to the intended recipient(s) based on the provided request.
     *
     * @param notification The notification request containing message content,
     *                    recipient information, and delivery preferences
     * @throws NotificationException if the notification cannot be delivered
     * @throws IllegalArgumentException if the notification request is invalid
     */
    void sendNotification(NotificationRequest notification);
}
