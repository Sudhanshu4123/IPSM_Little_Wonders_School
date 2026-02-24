package com.littlewonders.service;

import com.littlewonders.model.Enquiry;
import com.littlewonders.repository.EnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private EmailService emailService;

    public Enquiry saveEnquiry(Enquiry enquiry) {
        // Save to database
        Enquiry savedEnquiry = enquiryRepository.save(enquiry);

        // Send email notification
        sendEnquiryEmail(savedEnquiry);

        return savedEnquiry;
    }

    private void sendEnquiryEmail(Enquiry enquiry) {
        try {
            String subject = "New Enquiry from Little Wonders Website";
            String content = buildEmailContent(enquiry);
            emailService.sendEmailWithAttachments(subject, content);
        } catch (Exception e) {
            // Log error but don't fail the enquiry submission
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private String buildEmailContent(Enquiry enquiry) {
        return String.format(
                "<h3>New Enquiry Received</h3>" +
                        "<p><b>Child's Name:</b> %s</p>" +
                        "<p><b>Program:</b> %s</p>" +
                        "<p><b>Relation:</b> %s</p>" +
                        "<p><b>Parent's Name:</b> %s</p>" +
                        "<p><b>Phone:</b> %s</p>" +
                        "<p><b>Email:</b> %s</p>" +
                        "<p><b>Message:</b> %s</p>" +
                        "<p><b>Submitted At:</b> %s</p>",
                enquiry.getChildName(), enquiry.getProgram(), enquiry.getRelation(),
                enquiry.getParentName(), enquiry.getPhone(), enquiry.getEmail(),
                (enquiry.getMessage() != null ? enquiry.getMessage() : "N/A"),
                enquiry.getCreatedAt());
    }
}
