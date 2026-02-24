package com.littlewonders.service;

import com.littlewonders.model.CareerSubmission;
import com.littlewonders.repository.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CareerService {

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private EmailService emailService;

    public CareerSubmission processApplication(CareerSubmission submission, MultipartFile cv, MultipartFile photo) {
        // Save to database
        CareerSubmission savedSubmission = careerRepository.save(submission);

        // Send email
        try {
            String subject = "New Career Application: " + submission.getName();
            String content = buildEmailContent(submission);
            emailService.sendEmailWithAttachments(subject, content, cv, photo);
        } catch (Exception e) {
            System.err.println("Failed to send career application email: " + e.getMessage());
        }

        return savedSubmission;
    }

    private String buildEmailContent(CareerSubmission submission) {
        return String.format(
                "<h3>New Career Application Received</h3>" +
                        "<p><b>Name:</b> %s</p>" +
                        "<p><b>Position Applied For:</b> %s</p>" +
                        "<p><b>Email:</b> %s</p>" +
                        "<p><b>Contact:</b> %s</p>" +
                        "<p><b>Submitted At:</b> %s</p>",
                submission.getName(), submission.getPosition(), submission.getEmail(),
                submission.getContact(), submission.getCreatedAt());
    }
}
