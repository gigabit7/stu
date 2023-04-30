package com.example.stu.web.controller;

import com.example.stu.email.Email;
import com.example.stu.email.EmailService;
import com.example.stu.email.EmailType;
import com.example.stu.entity.*;
import com.example.stu.service.IBookingService;
import com.example.stu.service.IProviderService;
import com.example.stu.service.IServiceService;
import com.example.stu.web.dto.PayInPersonRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String bookServiceForm(@PathVariable Long id, Model model,
                                  @AuthenticationPrincipal User userDetails,
                                  @RequestParam("payment") String paymentMethod) {
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
    public String bookService(Model model, @Valid @ModelAttribute Booking booking,
                              BindingResult result,
                              @AuthenticationPrincipal User userDetails) throws MessagingException {
        booking.setUser(userDetails);
        if (!bookingService.dateRangeIsValid(booking.getStartDate(), booking.getEndDate())) {
            result.rejectValue("endDate", "error.booking", "The date range is invalid");
            Services services = serviceService.getById(booking.getServices().getId());
            booking.setServices(services);
            model.addAttribute("booking", booking);
            return "/booking/serviceBooking";
        }
        bookingService.save(booking);
        Email emailToUser =  emailService.buildEmailData(booking, userDetails.getEmail(), PRE_ORDER_USER);
        Email emailToProvider = emailService.buildEmailData(booking, booking.getServices().getServiceProvider().getUser().getEmail(),
                EmailType.PRE_ORDER_PROVIDER);

        emailService.sendEmail(emailToUser);
        emailService.sendEmail(emailToProvider);

        return "booking/successfulBooking";
    }

    @PostMapping("/pay-person")
    public String bookServiceInPerson(@Valid @ModelAttribute PayInPersonRequest payInPersonRequest,
                                      Model model,
                                      @AuthenticationPrincipal User userDetails,
                                      BindingResult result) throws MessagingException {
        Booking booking = payInPersonRequest.getBooking();
        booking.setUser(userDetails);
        if (!bookingService.dateRangeIsValid(booking.getStartDate(), booking.getEndDate())) {
            result.rejectValue("booking.endDate", "error.booking", "The date range is invalid");
            Services services = serviceService.getById(booking.getServices().getId());
            booking.setServices(services);
            payInPersonRequest.setBooking(booking);
            model.addAttribute("payInPersonRequest", payInPersonRequest);
            return "/booking/serviceBookingPerson";
        }
        bookingService.save(booking);
        model.addAttribute("booking", booking);
        Email emailToUser =  emailService.buildEmailData(booking, userDetails.getEmail(), PRE_ORDER_USER);
        Email emailToProvider = emailService.buildEmailData(booking, booking.getServices().getServiceProvider().getUser().getEmail(), EmailType.PRE_ORDER_PROVIDER);

        emailService.sendEmail(emailToUser);
        emailService.sendEmail(emailToProvider);
        return "booking/successfulBooking";
    }

    @PostMapping("/confirm/{id}")
    public String confirmBooking(@PathVariable(name = "id") Long id) throws MessagingException {
        Booking booking = bookingService.confirmBooking(id);
        Email emailToUser = emailService.buildEmailData(booking, booking.getUser().getEmail(), POST_ORDER_CONFIRM_USER);

        emailService.sendEmail(emailToUser);
        return "redirect:/?success=Order Confirmed Successfully!";
    }

    @PostMapping("/reject/{id}")
    public String rejectBooking(@PathVariable(name = "id") Long id) throws MessagingException {
        Booking booking = bookingService.rejectBooking(id);
        Email emailToUser = emailService.buildEmailData(booking, booking.getUser().getEmail(), POST_ORDER_REJECT_USER);

        emailService.sendEmail(emailToUser);
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
