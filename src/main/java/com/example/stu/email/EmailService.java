package com.example.stu.email;

import com.example.stu.entity.Booking;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;
/**
 * This interface is used to define the methods to be implemented by the EmailServiceImpl class.
 */
public interface EmailService {
    /**
     * This method is used to send email
     * @param recipientEmail The email address of the recipient.
     * @param subject The subject of the email.
     * @param emailVariables The variables to be used in the email template.
     * @param templateName The name of the template to be used.
     * @throws MessagingException If there is an error while sending the email.
     */
    @Async
    void sendEmail(String recipientEmail, String subject, Map<String, Object> emailVariables, String templateName) throws MessagingException;
    /**
     * This method is used to build the email data and send the email.
     * @param booking The booking object.
     * @param email The email address of the recipient.
     * @param emailType The type of email to be sent.
     * @throws MessagingException If there is an error while sending the email.
     */
    void buildEmailDataAndSend(@ModelAttribute Booking booking, String email, EmailType emailType) throws MessagingException ;
}
