package com.jobportal.controller;

import com.jobportal.model.Role;
import com.jobportal.model.User;
import com.jobportal.service.UserService;
import com.jobportal.service.OtpService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

private final UserService userService;
private final OtpService otpService;

public AuthController(UserService userService, OtpService otpService) {
    this.userService = userService;
    this.otpService = otpService;
}

@GetMapping("/login")
public String showLoginForm() {
    return "login";
}

@GetMapping("/register")
public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("roles", Role.values());
    return "register";
}

@PostMapping("/register")
public String registerUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           @RequestParam(value = "otp", required = false) String enteredOtp,
                           Model model) {

    // 🔹 Validation errors
    if (result.hasErrors()) {
        model.addAttribute("roles", Role.values());
        return "register";
    }

    // 🔹 Username already exists
    if (userService.findByUsername(user.getUsername()).isPresent()) {
        result.rejectValue("username", null, "There is already an account registered with that username");
        model.addAttribute("roles", Role.values());
        return "register";
    }

    // 🔹 OTP Validation
    if (enteredOtp == null || !otpService.validateOtp(user.getEmail(), enteredOtp)) {
        model.addAttribute("otpError", "Invalid or expired OTP");
        model.addAttribute("roles", Role.values());
        return "register";
    }

    // 🔹 Save user
    userService.saveUser(user);

    // 🔹 Clear OTP after success (important)
    otpService.clearOtp(user.getEmail());

    return "redirect:/login?success";
}


}
