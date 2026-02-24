package com.littlewonders.controller;

import com.littlewonders.model.CareerSubmission;
import com.littlewonders.service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CareerController {

    @Autowired
    private CareerService careerService;

    @PostMapping("/career/apply")
    public ResponseEntity<Map<String, String>> apply(
            @RequestParam("name") String name,
            @RequestParam("position") String position,
            @RequestParam("email") String email,
            @RequestParam("contact") String contact,
            @RequestParam("cv") MultipartFile cv,
            @RequestParam("photo") MultipartFile photo) {

        Map<String, String> response = new HashMap<>();
        try {
            CareerSubmission submission = new CareerSubmission();
            submission.setName(name);
            submission.setPosition(position);
            submission.setEmail(email);
            submission.setContact(contact);

            careerService.processApplication(submission, cv, photo);

            response.put("status", "success");
            response.put("message", "Application submitted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error submitting application: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
