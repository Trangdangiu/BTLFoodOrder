package com.example.testbotom.user;

import android.os.AsyncTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailTask extends AsyncTask<String, Void,Void> {

    @Override
    protected Void doInBackground(String... params) {
        String email = params[0];
        String otp = params[1];
        // Cấu hình thông tin gửi email
        final String username = "Thanhnamnb1004@gmail";
        final String password = "ehgg yzrn eoqo sqlv";

        // Thiết lập thuộc tính cho Session
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Tạo một phiên làm việc với thông tin xác thực
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }

        });
        try {
            // Tạo một đối tượng MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Mã OTP của bạn");
            message.setText("Mã OTP của bạn là: " + otp);

            // Gửi email
            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}