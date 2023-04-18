package com.example.stu.web.controller;

import com.example.stu.entity.Booking;
import com.example.stu.entity.ServiceProvider;
import com.example.stu.entity.User;
import com.example.stu.service.IBookingService;
import com.example.stu.service.IProviderService;
import com.example.stu.service.IServiceService;
import com.example.stu.service.IUserService;
import com.example.stu.web.dto.UserDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AuthController {

    private final IUserService userService;

    private final IProviderService providerService;

    private final IBookingService bookingService;

    private final IServiceService serviceService;

    JavaMailSender javaMailSender;

    @GetMapping("/")
    public String home(Model model, Authentication authentication, @AuthenticationPrincipal User userDetails) {

        if (authentication != null && authentication.isAuthenticated()) {
            boolean isProvider = providerService.checkIfProviderFromAuthentication(authentication);

            if (isProvider) {
                ServiceProvider serviceProvider = providerService.findByUserEmail(authentication.getName());
                model.addAttribute("providerId", serviceProvider.getId());
                if ((serviceProvider.getService() == null)) {
                    model.addAttribute("hasService", false);
                } else {
                    List<Booking> bookingRequests = bookingService.getUnconfirmedBookingsByProviderId(serviceProvider.getId());
                    model.addAttribute("hasService", true);
                    model.addAttribute("booking_requests", bookingRequests);
                }
            }

            model.addAttribute("isServiceProvider", isProvider);
            model.addAttribute("user", userDetails);
        }

        return "index";
    }


    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/edit")
    public String editUser(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName());
        model.addAttribute("user", userService.convertEntityToDto(user));
        return "edit";
    }

    @PostMapping("/edit")
    public String editUser(@Valid @ModelAttribute("user") UserDto user,
                           BindingResult result,
                           Model model) {
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            existing.setPhone(user.getPhone());
            existing.setFirstName(user.getFirstName());
            existing.setLastName(user.getLastName());
            userService.update(existing);
        }else{
            result.rejectValue("email", null, "There is no account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "edit";
        }
        return "redirect:/edit?success";
    }
}
