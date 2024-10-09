package com.example.testbotom.user;

import java.security.SecureRandom;

public class OtpGenerator {
    private static final String CHARACTERS = "0123456789";
    private static final int OTP_LENGTH = 4; // Độ dài của OTP

    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otpBuilder = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            otpBuilder.append(CHARACTERS.charAt(index));
        }

        return otpBuilder.toString();
    }
}
