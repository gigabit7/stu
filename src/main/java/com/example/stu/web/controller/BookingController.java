package com.example.stu.web.controller;

import com.example.stu.email.EmailService;
import com.example.stu.email.EmailType;
import com.example.stu.entity.*;
import com.example.stu.service.IBookingService;
import com.example.stu.service.IProviderService;
import com.example.stu.service.IServiceService;
import com.example.stu.web.dto.PayInPersonRequest;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.stu.email.EmailType.*;
/**
 * This controller will handle all requests related to booking services.
 */
@Controller
@RequestMapping("/booking")
@AllArgsConstructor
public class BookingController {
    private final IServiceService serviceService;
    private final IBookingService bookingService;
    private final EmailService emailService;
    private final IProviderService providerService;

    @GetMapping("{id}")
    public String bookServiceForm(@PathVariable Long id, Model model, @AuthenticationPrincipal User userDetails, @RequestParam("payment") String paymentMethod) {
        Services service = serviceService.getById(id);
        Booking booking = new Booking();
        booking.setServices(service);
        if (paymentMethod.equals("online")) {
            booking.setPaymentMethod(PaymentMethod.ONLINE);
            model.addAttribute("booking", booking);
            return "/booking/serviceBooking";
        } else if (paymentMethod.equals("person")) {
            booking.setPaymentMethod(PaymentMethod.IN_PERSON);
            PayInPersonRequest payInPersonRequest = new PayInPersonRequest(booking, userDetails.getFullName(), userDetails.getEmail());
            model.addAttribute("payInPersonRequest", payInPersonRequest);
            return "/booking/serviceBookingPerson";
        } else {
            return "redirect:/booking?error=Invalid payment method";
        }

    }

    @PostMapping("/pay-online")
    public String bookService(Model model, @ModelAttribute Booking booking, @AuthenticationPrincipal User userDetails) throws MessagingException {
        booking.setUser(userDetails);
        bookingService.save(booking);
        model.addAttribute("booking", booking);
        emailService.buildEmailDataAndSend(booking, userDetails.getEmail(), PRE_ORDER_USER);
        emailService.buildEmailDataAndSend(booking, booking.getServices().getServiceProvider().getUser().getEmail(), EmailType.PRE_ORDER_PROVIDER);
        return "booking/successfulBooking";
    }

    @PostMapping("/pay-person")
    public String bookServiceInPerson(@ModelAttribute PayInPersonRequest payInPersonRequest, Model model, @AuthenticationPrincipal User userDetails) throws MessagingException {
        payInPersonRequest.getBooking().setUser(userDetails);
        Booking booking = bookingService.save(payInPersonRequest.getBooking());
        model.addAttribute("booking", payInPersonRequest.getBooking());
        emailService.buildEmailDataAndSend(booking, userDetails.getEmail(), PRE_ORDER_USER);
        emailService.buildEmailDataAndSend(booking, booking.getServices().getServiceProvider().getUser().getEmail(), EmailType.PRE_ORDER_PROVIDER);
        return "booking/successfulBooking";
    }

    @PostMapping("/confirm/{id}")
    public String confirmBooking(@PathVariable(name = "id") Long id) throws MessagingException {
        Booking booking = bookingService.confirmBooking(id);
        emailService.buildEmailDataAndSend(booking, booking.getUser().getEmail(), POST_ORDER_CONFIRM_USER);
        return "redirect:/?success=Order Confirmed Successfully!";
    }

    @PostMapping("/reject/{id}")
    public String rejectBooking(@PathVariable(name = "id") Long id) throws MessagingException {
        Booking booking = bookingService.rejectBooking(id);
        emailService.buildEmailDataAndSend(booking, booking.getUser().getEmail(), POST_ORDER_REJECT_USER);
        return "redirect:/?success=Order Rejected Successfully!";
    }

    @PreAuthorize(value = "PROVIDER")
    @GetMapping
    public String getConfirmedBookings(Authentication authentication, Model model) {
        ServiceProvider serviceProvider = providerService.findByUserEmail(authentication.getName());
        List<Booking> bookings = bookingService.getConfirmedBookingsByProviderId(serviceProvider.getId());
        model.addAttribute("bookings", bookings);
        return "/booking/bookings";
    }
}
