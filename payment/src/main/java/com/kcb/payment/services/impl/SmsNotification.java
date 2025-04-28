package com.kcb.payment.services.impl;

import com.kcb.payment.dto.NotificationRequest;
import com.kcb.payment.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsNotification implements NotificationService {
    @Override
    public void sendNotification(NotificationRequest notification) {
        log.info("Sending notification to phone: {}", notification.getPhone());
    }
}
