package com.example.stu.email;

import com.example.stu.entity.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {
    @Mock
    private JavaMailSender emailSender;

    @Mock
    private Booking booking;


    @Mock
    private ITemplateEngine emailTemplateEngine;

    @InjectMocks
    private EmailServiceImpl emailService;

    private final User TEST_USER = new User(1231333L,"TEST", "USER", "example@mail.com", "pass", "01212",
            "TEST USER", List.of(new Role(RoleName.ROLE_USER)));

    @Test
    public void sendEmail_WithValidParameters_ShouldSendEmail() throws MessagingException {
        // Arrange
        String recipientEmail = "recipient@example.com";
        String subject = "Test Email";
        String templateName = "test-template";
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", "John Doe");
        String emailContent = "<html>Hello, [[${name}]]!</html>";
        when(emailTemplateEngine.process(eq(templateName), any(Context.class))).thenReturn(emailContent);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        Email email = Email.builder()
                .recipientEmail(recipientEmail)
                .subject(subject)
                .templateName(templateName)
                .emailVariables(emailVariables)
                .build();
        // Act
        emailService.sendEmail(email);

        // Assert
        Mockito.verify(emailSender).send(any(MimeMessage.class));
    }

    @Test
    public void sendEmail_WithNullTemplateName_ShouldThrowIllegalArgumentException() throws MessagingException {
        // Arrange
        String recipientEmail = "recipient@example.com";
        String subject = "Test Email";
        String templateName = null;
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", "John Doe");

        Email email = Email.builder()
                .recipientEmail(recipientEmail)
                .subject(subject)
                .templateName(templateName)
                .emailVariables(emailVariables)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> emailService.sendEmail(email));
    }

    @Test
    public void sendEmail_WithNullRecipient_ShouldThrowIllegalArgumentException() throws MessagingException {
        // Arrange
        String recipientEmail = null;
        String subject = "Test Email";
        String templateName = "test-template";
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", "John Doe");

        Email email = Email.builder()
                .recipientEmail(recipientEmail)
                .subject(subject)
                .templateName(templateName)
                .emailVariables(emailVariables)
                .build();

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(email);
        });
        assertEquals("Email recipient cannot be null or empty", exception.getMessage());
    }

    @Test
    public void sendEmail_WithNullSubject_ShouldThrowException() throws MessagingException {
        // Arrange
        String recipientEmail = "recipient@example.com";
        String subject = null;
        String templateName = "test-template";
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", "John Doe");

        Email email = Email.builder()
                .recipientEmail(recipientEmail)
                .subject(subject)
                .templateName(templateName)
                .emailVariables(emailVariables)
                .build();
        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(email);
        });
        assertEquals("Email subject cannot be null or empty", exception.getMessage());
    }

    @Test
    public void sendEmail_WithNullEmailVariables_ShouldThrowException() throws MessagingException {
        // Arrange
        String recipientEmail = "recipient@example.com";
        String subject = "Test Email";
        String templateName = "test-template";
        Map<String, Object> emailVariables = null;

        Email email = Email.builder()
                .recipientEmail(recipientEmail)
                .subject(subject)
                .templateName(templateName)
                .emailVariables(emailVariables)
                .build();

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendEmail(email);
        });
        assertEquals("Email variables cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testBuildEmailData() {
        LocalDateTime now = LocalDateTime.now();
        when(booking.getServices()).thenReturn(new Services(0L,
                "Test Service",
                BigDecimal.TEN,new ServiceProvider()));
        when(booking.getCreatedAt()).thenReturn(now);
        when(booking.getAddress()).thenReturn("123 Test St");
        when(booking.getPaymentMethod()).thenReturn(PaymentMethod.ONLINE);
        when(booking.getStartDate()).thenReturn(LocalDate.now().plusDays(1));
        when(booking.getEndDate()).thenReturn(LocalDate.now().plusDays(2));
        when(booking.getUser()).thenReturn(TEST_USER);

        EmailType emailType = EmailType.PRE_ORDER_PROVIDER;
        Email email = emailService.buildEmailData(booking, "test@test.com", emailType);

        assertEquals("test@test.com", email.getRecipientEmail());
        assertEquals("New Booking", email.getSubject());
        assertEquals("email/pre-order-provider", email.getTemplateName());
        assertEquals("New Booking!", email.getEmailVariables().get("title"));
        assertEquals("Test Service", email.getEmailVariables().get("service_name"));
        assertEquals(now, email.getEmailVariables().get("created_at"));
        assertEquals("123 Test St", email.getEmailVariables().get("address"));
        assertEquals(PaymentMethod.ONLINE, email.getEmailVariables().get("payment_method"));
        assertEquals(LocalDate.now().plusDays(1), email.getEmailVariables().get("from"));
        assertEquals(LocalDate.now().plusDays(2), email.getEmailVariables().get("to"));
        assertEquals(TEST_USER.getFullName(), email.getEmailVariables().get("ordered_by"));
    }
}