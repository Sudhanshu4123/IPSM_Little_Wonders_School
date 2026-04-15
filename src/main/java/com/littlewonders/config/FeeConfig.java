package com.littlewonders.config;

import org.springframework.stereotype.Component;

@Component
public class FeeConfig {
    
    // One-time fees
    private final Double registrationFee = 1000.0;
    private final Double admissionFee = 5000.0;
    private final Double annualFee = 5000.0;
    private final Double uniformFee = 2500.0;
    private final Double booksFee = 3000.0;
    private final Double bagFee = 0.0;
    private final Double idCardFee = 0.0;
    private final Double stationaryFee = 0.0;
    private final Double celebrationFee = 0.0;
    private final Double activityFee = 0.0;
    private final Double yogaMusicDanceFee = 0.0;

    // Getters for Thymeleaf and backend usage
    public Double getRegistrationFee() { return registrationFee; }
    public Double getAdmissionFee() { return admissionFee; }
    public Double getAnnualFee() { return annualFee; }
    public Double getUniformFee() { return uniformFee; }
    public Double getBooksFee() { return booksFee; }
    public Double getBagFee() { return bagFee; }
    public Double getIdCardFee() { return idCardFee; }
    public Double getStationaryFee() { return stationaryFee; }
    public Double getCelebrationFee() { return celebrationFee; }
    public Double getActivityFee() { return activityFee; }
    public Double getYogaMusicDanceFee() { return yogaMusicDanceFee; }

    // Logic for tuition fee based on Course Program
    public Double getTuitionFee(String course) {
        if (course == null) return 2000.0;
        switch (course.trim().toUpperCase()) {
            case "TODDLER":
                return 2000.0;
            case "PRE-NURSERY":
            case "NURSERY":
                return 2500.0;
            case "LKG":
            case "UKG":
            case "PREP":
                return 3000.0;
            default:
                return 2000.0;
        }
    }
}
