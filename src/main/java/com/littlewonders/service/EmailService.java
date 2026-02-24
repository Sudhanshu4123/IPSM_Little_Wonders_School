package com.littlewonders.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${enquiry.recipient.email}")
    private String recipientEmail;

    public void sendEmailWithAttachments(String subject, String content, MultipartFile... attachments)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, true); // true for HTML

        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if (file != null && !file.isEmpty()) {
                    helper.addAttachment(file.getOriginalFilename(), file);
                }
            }
        }

        mailSender.send(message);
    }
}
