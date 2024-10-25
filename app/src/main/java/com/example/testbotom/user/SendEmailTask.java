package com.example.testbotom.user;

import android.os.AsyncTask;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailTask extends AsyncTask<String, Void, Void> {

    // Configuration for email
    final String fromUsername = "Thanhnamnb1004@gmail.com";
    final String password = "ehgg yzrn eoqo sqlv";

    @Override
    protected Void doInBackground(String... params) {
        String email = params[0]; // Recipient email
        String otp = params[1];    // OTP to send

        Session session = createEmailSession();
        String subject = "Mã OTP của bạn";
        String content = generateEmailContent(otp);

        sendEmail(session, email, subject, content);
        return null;
    }

    // Method to create email session with properties and authentication
    private Session createEmailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Creating the session with authenticator
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromUsername, password);
            }
        });
    }

    // Method to generate the email content dynamically
    private String generateEmailContent(String otp) {
        return "<h1>Mã OTP của bạn là: <strong>" + otp + "</strong></h1>";
    }

    // Method to send the email
    private void sendEmail(Session session, String recipientEmail, String subject, String content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            // sender
            message.setFrom(new InternetAddress(fromUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(content, "text/HTML; charset=UTF-8");

            // Sending email
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}