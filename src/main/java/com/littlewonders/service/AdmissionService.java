package com.littlewonders.service;
// Refreshed

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

    @Autowired
    private com.littlewonders.repository.UserRepository userRepository;

    @Autowired
    private com.littlewonders.repository.RoleRepository roleRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AdmissionApplication processApplication(AdmissionApplication application,
            MultipartFile parentPhoto,
            MultipartFile aadharCard,
            MultipartFile panCard,
            MultipartFile childPhoto,
            MultipartFile birthCertificate) {
        // Save to database
        AdmissionApplication savedApplication = admissionRepository.save(application);

        // Create User account for student panel
        if (savedApplication.getFatherPhone() != null && !userRepository.existsByUsername(savedApplication.getFatherPhone())) {
            com.littlewonders.model.User user = new com.littlewonders.model.User();
            user.setUsername(savedApplication.getFatherPhone());
            user.setPassword(passwordEncoder.encode("123456")); // Default password for new students
            user.setFullName(savedApplication.getFirstName() + " " + savedApplication.getLastName());
            user.setEmail(savedApplication.getEmail());
            user.setPhone(savedApplication.getFatherPhone());
            user.setRegistrationNumber("LW" + savedApplication.getId());
            user.setPendingFees(0.0);

            com.littlewonders.model.Role userRole = roleRepository.findByName("ROLE_USER").orElse(null);
            if (userRole != null) {
                java.util.Set<com.littlewonders.model.Role> roles = new java.util.HashSet<>();
                roles.add(userRole);
                user.setRoles(roles);
            }
            userRepository.save(user);
        }

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
