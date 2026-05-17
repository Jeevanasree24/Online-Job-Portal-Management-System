package com.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {

    private final Map<String, OtpData> otpStorage = new HashMap<>();
    private final SecureRandom random = new SecureRandom();

    @Autowired
    private JavaMailSender mailSender;

    // Generate OTP
    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Send OTP
    public void sendOtp(String email) {
        String otp = generateOtp();

        OtpData data = new OtpData();
        data.setOtp(otp);
        data.setExpiryTime(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 min

        otpStorage.put(email, data);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP for Registration");
        message.setText("Your OTP is: " + otp);

        mailSender.send(message);
    }

    // Validate OTP
    public boolean validateOtp(String email, String otp) {
        OtpData data = otpStorage.get(email);

        if (data == null) return false;

        // Check expiry
        if (System.currentTimeMillis() > data.getExpiryTime()) {
            otpStorage.remove(email);
            return false;
        }

        return data.getOtp().equals(otp);
    }

    // Clear OTP after success
    public void clearOtp(String email) {
        otpStorage.remove(email);
    }

    // Inner class
    static class OtpData {
        private String otp;
        private long expiryTime;

        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }

        public long getExpiryTime() { return expiryTime; }
        public void setExpiryTime(long expiryTime) { this.expiryTime = expiryTime; }
    }
}