package com.ticketsales.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(String toEmail, String firstName,
                                        String bookingReference, String eventName,
                                        String eventDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@ticketsales.com");
        message.setTo(toEmail);
        message.setSubject("Booking Confirmation - " + bookingReference);
        message.setText(
                "Dear " + firstName + ",\n\n" +
                        "Your booking has been confirmed!\n\n" +
                        "Booking Reference: " + bookingReference + "\n" +
                        "Event: " + eventName + "\n" +
                        "Date: " + eventDate + "\n\n" +
                        "Thank you for your purchase!\n\n" +
                        "Ticket Sales Team"
        );
        mailSender.send(message);
    }
}