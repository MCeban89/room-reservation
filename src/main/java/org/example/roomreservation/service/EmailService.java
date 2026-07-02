package org.example.roomreservation.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text, byte[] iscBytes,byte[] qrCodeBytes) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = text + "<br/><br/><h3>Codul tău de acces QR:</h3>"
                    + "<img src='cid:qrCodeImage' alt='QR Code Check-in'/>";
            helper.setText(htmlContent, true);

            ByteArrayDataSource icsDataSource = new ByteArrayDataSource(iscBytes, "text/calendar");
            helper.addAttachment("rezervare.ics", icsDataSource);

            ByteArrayDataSource qrDataSource = new ByteArrayDataSource(qrCodeBytes, "image/png");
            helper.addInline("qrCodeImage", qrDataSource);

            mailSender.send(mimeMessage);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
