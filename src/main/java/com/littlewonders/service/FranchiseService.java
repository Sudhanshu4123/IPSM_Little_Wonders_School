package com.littlewonders.service;

import com.littlewonders.model.FranchiseEnquiry;
import com.littlewonders.repository.FranchiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FranchiseService {

    @Autowired
    private FranchiseRepository franchiseRepository;

    @Autowired
    private EmailService emailService;

    public FranchiseEnquiry processEnquiry(FranchiseEnquiry enquiry) {
        // Save to database
        FranchiseEnquiry savedEnquiry = franchiseRepository.save(enquiry);

        // Send email
        try {
            String subject = "New Franchise Enquiry: " + enquiry.getName();
            String content = buildEmailContent(enquiry);
            emailService.sendEmailWithAttachments(subject, content);
        } catch (Exception e) {
            System.err.println("Failed to send franchise enquiry email: " + e.getMessage());
        }

        return savedEnquiry;
    }

    private String buildEmailContent(FranchiseEnquiry enquiry) {
        return String.format(
                "<h3>New Franchise Enquiry Received</h3>" +
                        "<p><b>Name:</b> %s</p>" +
                        "<p><b>Email:</b> %s</p>" +
                        "<p><b>Contact:</b> %s</p>" +
                        "<p><b>State:</b> %s</p>" +
                        "<p><b>Area:</b> %s</p>" +
                        "<p><b>Submitted At:</b> %s</p>",
                enquiry.getName(), enquiry.getEmail(), enquiry.getContact(),
                enquiry.getState(), enquiry.getArea(), enquiry.getCreatedAt());
    }
}
