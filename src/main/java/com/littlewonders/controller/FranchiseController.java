package com.littlewonders.controller;

import com.littlewonders.model.FranchiseEnquiry;
import com.littlewonders.service.FranchiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class FranchiseController {

    @Autowired
    private FranchiseService franchiseService;

    @PostMapping("/franchise/enquiry")
    public ResponseEntity<Map<String, String>> submitEnquiry(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("contact") String contact,
            @RequestParam("state") String state,
            @RequestParam("area") String area) {

        Map<String, String> response = new HashMap<>();
        try {
            FranchiseEnquiry enquiry = new FranchiseEnquiry();
            enquiry.setName(name);
            enquiry.setEmail(email);
            enquiry.setContact(contact);
            enquiry.setState(state);
            enquiry.setArea(area);

            franchiseService.processEnquiry(enquiry);

            response.put("status", "success");
            response.put("message", "Enquiry submitted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error submitting enquiry: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
