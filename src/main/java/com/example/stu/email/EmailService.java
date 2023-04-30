package com.example.stu.email;

import com.example.stu.entity.Booking;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * This interface is used to define the methods to be implemented by the EmailServiceImpl class.
 */
public interface EmailService {
   /**
     * This method is used to send the email.
     * @param email The email object.
     * @throws MessagingException If there is an error while sending the email.
     */
    @Async
    void sendEmail(Email email) throws MessagingException;
    /**
     * This method is used to build the email data and send the email.
     * @param booking The booking object.
     * @param email The email address of the recipient.
     * @param emailType The type of email to be sent.
     * @throws MessagingException If there is an error while sending the email.
     */
    Email buildEmailData(@ModelAttribute Booking booking, String email, EmailType emailType) throws MessagingException ;
}
