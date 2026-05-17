package com.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.service.OtpService;

@RestController
@RequestMapping("/auth")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        System.out.println("SEND OTP HIT");
        otpService.sendOtp(email);
        return "OTP sent successfully!";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp) {

        System.out.println("VERIFY OTP HIT");

        if (otpService.validateOtp(email, otp)) {
            return "OTP verified!";
        } else {
            return "Invalid or expired OTP!";
        }
    }
}