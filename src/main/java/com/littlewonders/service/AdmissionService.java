package com.littlewonders.service;

import com.littlewonders.model.AdmissionApplication;
import com.littlewonders.repository.AdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdmissionService {

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private EmailService emailService;

    public AdmissionApplication processApplication(AdmissionApplication application,
            MultipartFile parentPhoto,
            MultipartFile aadharCard,
            MultipartFile panCard,
            MultipartFile childPhoto,
            MultipartFile birthCertificate) {
        // Save to database
        AdmissionApplication savedApplication = admissionRepository.save(application);

        // Send email
        try {
            String subject = "New Admission Application: " + application.getFirstName() + " "
                    + application.getLastName();
            String content = buildEmailContent(application);
            emailService.sendEmailWithAttachments(subject, content,
                    parentPhoto, aadharCard, panCard, childPhoto, birthCertificate);
        } catch (Exception e) {
            System.err.println("Failed to send admission application email: " + e.getMessage());
        }

        return savedApplication;
    }

    private String buildEmailContent(AdmissionApplication application) {
        StringBuilder content = new StringBuilder();
        content.append("<h3>New Admission Application Received</h3>");

        content.append("<h4>Child Information</h4>");
        content.append("<p><b>Name:</b> ").append(application.getFirstName()).append(" ")
                .append(application.getLastName()).append("</p>");
        content.append("<p><b>Nick Name:</b> ").append(application.getNickName()).append("</p>");
        content.append("<p><b>Program Applied:</b> ").append(application.getProgramApplied()).append("</p>");
        content.append("<p><b>Date of Birth:</b> ").append(application.getDob()).append("</p>");
        content.append("<p><b>Age:</b> ").append(application.getAge()).append("</p>");
        content.append("<p><b>Gender:</b> ").append(application.getGender()).append("</p>");
        content.append("<p><b>Blood Group:</b> ").append(application.getBloodGroup()).append("</p>");
        content.append("<p><b>Number of Brothers:</b> ").append(application.getNumBrothers()).append("</p>");
        content.append("<p><b>Number of Sisters:</b> ").append(application.getNumSisters()).append("</p>");

        content.append("<h4>Parents Information</h4>");
        content.append("<p><b>Father's Name:</b> ").append(application.getFatherName()).append("</p>");
        content.append("<p><b>Mother's Name:</b> ").append(application.getMotherName()).append("</p>");
        content.append("<p><b>Father's Phone:</b> ").append(application.getFatherPhone()).append("</p>");
        content.append("<p><b>Mother's Phone:</b> ").append(application.getMotherPhone()).append("</p>");
        content.append("<p><b>Alternate Phone:</b> ").append(application.getAlternatePhone()).append("</p>");
        content.append("<p><b>Email:</b> ").append(application.getEmail()).append("</p>");
        content.append("<p><b>Father's Occupation:</b> ").append(application.getFatherOccupation()).append("</p>");
        content.append("<p><b>Mother's Occupation:</b> ").append(application.getMotherOccupation()).append("</p>");
        content.append("<p><b>Category:</b> ").append(application.getCategory()).append("</p>");
        content.append("<p><b>Total Children:</b> ").append(application.getTotalChildren()).append("</p>");
        content.append("<p><b>Address:</b> ").append(application.getAddress()).append("</p>");

        content.append("<p><b>Submitted At:</b> ").append(application.getCreatedAt()).append("</p>");

        return content.toString();
    }
}
