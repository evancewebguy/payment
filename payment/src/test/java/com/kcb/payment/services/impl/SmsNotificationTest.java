package com.kcb.payment.services.impl;

import static org.mockito.Mockito.*;

import com.kcb.payment.dto.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;

class SmsNotificationTest {

    @Mock
    private Logger logger;

    @Mock
    private NotificationRequest notificationRequest;

    private SmsNotification smsNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        smsNotification = new SmsNotification();
    }

    @Test
    void testSendNotification_logsPhoneNumber() {
        // Arrange
        String phone = "1234567890";
        when(notificationRequest.getPhone()).thenReturn(phone);

        // Act
        smsNotification.sendNotification(notificationRequest);

        // Assert
        verify(notificationRequest, times(1)).getPhone(); // Ensure getPhone() was called once
    }

    @Test
    void testSendNotification_noPhone() {
        // Arrange
        when(notificationRequest.getPhone()).thenReturn(null);

        // Act
        smsNotification.sendNotification(notificationRequest);

        // Assert
        verify(notificationRequest, times(1)).getPhone();
    }
}
