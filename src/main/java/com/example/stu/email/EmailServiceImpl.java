package com.example.stu.email;

import com.example.stu.entity.Booking;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class is used to implement the methods defined in the EmailService interface.
 * See the EmailService interface for more details.
 */
@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);


    @Override
    public void sendEmail(String recipientEmail, String subject, Map<String, Object> emailVariables, String templateName) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("service-to-you@stu.com");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);

        String content = templateEngine.process(templateName, new Context(Locale.getDefault(), emailVariables));
        helper.setText(content, true);
        LOGGER.info("Sending email to: " + recipientEmail);
        emailSender.send(message);
        LOGGER.info("Email Sent To: " + recipientEmail);
    }

    @Override
    public void buildEmailDataAndSend(Booking booking, String email, EmailType emailType) {
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("service_name", booking.getServices().getName());
        emailVariables.put("created_at", booking.getCreatedAt());
        emailVariables.put("address", booking.getAddress());
        emailVariables.put("payment_method", booking.getPaymentMethod());
        emailVariables.put("from", booking.getStartDate());
        emailVariables.put("to", booking.getEndDate());

        String title = "";
        String templateName;
        String subject = "";
        switch (emailType) {
            case POST_ORDER_CONFIRM_USER:
                templateName = "post-order-confirm-user";
                break;
            case POST_ORDER_REJECT_USER:
                title = "Booking Rejected!";
                templateName = "post-order-reject-user";
                subject = "Booking Rejection";
                break;
            case PRE_ORDER_USER:
                templateName = "pre-order-user";
                break;
            case PRE_ORDER_PROVIDER:
                title = "New Booking!";
                emailVariables.put("ordered_by", booking.getUser().getFullName());
                templateName = "pre-order-provider";
                subject = "New Booking";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + emailType);
        }
        emailVariables.put("title", title.isEmpty() ? "Booking Confirmation" : title);
        templateName = String.format("email/%s", templateName);
        subject = subject.isEmpty() ? "Booking Confirmation" : subject;

        try {
            sendEmail(email, subject, emailVariables, templateName);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email. Please try again later.");
        }
    }

}
