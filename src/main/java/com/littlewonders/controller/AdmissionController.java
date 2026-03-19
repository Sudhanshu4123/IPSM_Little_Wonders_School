package com.littlewonders.controller;

import com.littlewonders.model.AdmissionApplication;
import com.littlewonders.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AdmissionController {

    @Autowired
    private AdmissionService admissionService;

    @PostMapping("/admission/apply")
    public ResponseEntity<Map<String, String>> apply(
            @RequestParam("fatherName") String fatherName,
            @RequestParam("motherName") String motherName,
            @RequestParam("fatherPhone") String fatherPhone,
            @RequestParam("motherPhone") String motherPhone,
            @RequestParam(value = "alternatePhone", required = false) String alternatePhone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam("fatherOccupation") String fatherOccupation,
            @RequestParam("motherOccupation") String motherOccupation,
            @RequestParam("address") String address,
            @RequestParam("totalChildren") Integer totalChildren,
            @RequestParam("category") String category,
            @RequestParam("programApplied") String programApplied,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "bloodGroup", required = false) String bloodGroup,
            @RequestParam("dob") String dob,
            @RequestParam("age") Integer age,
            @RequestParam("gender") String gender,
            @RequestParam(value = "numBrothers", required = false, defaultValue = "0") Integer numBrothers,
            @RequestParam(value = "numSisters", required = false, defaultValue = "0") Integer numSisters,
            @RequestParam("parentPhoto") MultipartFile parentPhoto,
            @RequestParam("aadharCard") MultipartFile aadharCard,
            @RequestParam("panCard") MultipartFile panCard,
            @RequestParam("childPhoto") MultipartFile childPhoto,
            @RequestParam("birthCertificate") MultipartFile birthCertificate,
            @RequestParam("session") String session) {

        Map<String, String> response = new HashMap<>();
        try {
            AdmissionApplication app = new AdmissionApplication();
            app.setSession(session);
            app.setFatherName(fatherName);
            app.setMotherName(motherName);
            app.setFatherPhone(fatherPhone);
            app.setMotherPhone(motherPhone);
            app.setAlternatePhone(alternatePhone);
            app.setEmail(email);
            app.setFatherOccupation(fatherOccupation);
            app.setMotherOccupation(motherOccupation);
            app.setAddress(address);
            app.setTotalChildren(totalChildren);
            app.setCategory(category);
            app.setProgramApplied(programApplied);
            app.setFirstName(firstName);
            app.setLastName(lastName);
            app.setNickName(nickName);
            app.setBloodGroup(bloodGroup);
            app.setDob(dob);
            app.setAge(age);
            app.setGender(gender);
            app.setNumBrothers(numBrothers != null ? numBrothers : 0);
            app.setNumSisters(numSisters != null ? numSisters : 0);

            admissionService.processApplication(app, parentPhoto, aadharCard, panCard, childPhoto, birthCertificate);

            response.put("status", "success");
            response.put("message", "Admission application submitted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error submitting application: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
