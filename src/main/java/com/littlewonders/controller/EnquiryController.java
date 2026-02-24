package com.littlewonders.controller;

import com.littlewonders.model.Enquiry;
import com.littlewonders.service.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    @PostMapping("/submit-enquiry")
    public ResponseEntity<Map<String, String>> submitEnquiry(
            @RequestParam("childName") String childName,
            @RequestParam(value = "program", required = false, defaultValue = "General Inquiry") String program,
            @RequestParam(value = "relation", required = false, defaultValue = "Not Specified") String relation,
            @RequestParam("parentName") String parentName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "message", required = false) String message) {

        try {
            Enquiry enquiry = new Enquiry();
            enquiry.setChildName(childName);
            enquiry.setProgram(program);
            enquiry.setRelation(relation);
            enquiry.setParentName(parentName);
            enquiry.setPhone(phone);
            enquiry.setEmail(email);
            enquiry.setMessage(message);

            enquiryService.saveEnquiry(enquiry);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Thank you! Your enquiry has been submitted successfully.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Sorry, there was an error submitting your enquiry. Please try again.");

            return ResponseEntity.status(500).body(response);
        }
    }
}
