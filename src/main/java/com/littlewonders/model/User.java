package com.littlewonders.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;
    private String email;
    private String phone; // This can be used for primary login contact

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // For student specific data
    @Column(unique = true)
    private String rollNumber;
    private String dateOfBirth;
    private String registrationNumber;
    private Double pendingFees;
    private String session;
    private String course;
    
    // New fields for detailed registration
    private String fatherName;
    private String motherName;
    private String fatherPhone;
    private String motherPhone;
    private String alternatePhone;
    private String fatherOccupation;
    private String motherOccupation;
    
    // Fee Details
    private Double registrationFee;
    private Double admissionFee;
    private Double annualFee;
    private Double tuitionFee;
    private Double uniformFee;
    private Double booksFee;
    private Double bagFee;
    private Double idCardFee;
    private Double stationaryFee;
    private Double celebrationFee;
    private Double activityFee;
    private Double yogaMusicDanceFee;
    
    @Column(length = 1000)
    private String address;
    private Integer totalChildren;
    private String gender;
    private String bloodGroup;
    private Integer age;
    
    // Document/Photo paths
    private String childPhotoPath;
    private String motherPhotoPath;
    private String fatherPhotoPath;
    private String guardianPhotoPath;
    private String birthCertificatePath;
    private String aadharCardPath;
    private String panCardPath;

    // Manual Getters/Setters to ensure compilation
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public Double getPendingFees() { return pendingFees; }
    public void setPendingFees(Double pendingFees) { this.pendingFees = pendingFees; }
    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public String getFatherPhone() { return fatherPhone; }
    public void setFatherPhone(String fatherPhone) { this.fatherPhone = fatherPhone; }
    public String getMotherPhone() { return motherPhone; }
    public void setMotherPhone(String motherPhone) { this.motherPhone = motherPhone; }
    public String getAlternatePhone() { return alternatePhone; }
    public void setAlternatePhone(String alternatePhone) { this.alternatePhone = alternatePhone; }
    public String getFatherOccupation() { return fatherOccupation; }
    public void setFatherOccupation(String fatherOccupation) { this.fatherOccupation = fatherOccupation; }
    public String getMotherOccupation() { return motherOccupation; }
    public void setMotherOccupation(String motherOccupation) { this.motherOccupation = motherOccupation; }
    
    public Double getRegistrationFee() { return registrationFee; }
    public void setRegistrationFee(Double registrationFee) { this.registrationFee = registrationFee; }
    public Double getAdmissionFee() { return admissionFee; }
    public void setAdmissionFee(Double admissionFee) { this.admissionFee = admissionFee; }
    public Double getAnnualFee() { return annualFee; }
    public void setAnnualFee(Double annualFee) { this.annualFee = annualFee; }
    public Double getTuitionFee() { return tuitionFee; }
    public void setTuitionFee(Double tuitionFee) { this.tuitionFee = tuitionFee; }
    public Double getUniformFee() { return uniformFee; }
    public void setUniformFee(Double uniformFee) { this.uniformFee = uniformFee; }
    public Double getBooksFee() { return booksFee; }
    public void setBooksFee(Double booksFee) { this.booksFee = booksFee; }
    public Double getBagFee() { return bagFee; }
    public void setBagFee(Double bagFee) { this.bagFee = bagFee; }
    public Double getIdCardFee() { return idCardFee; }
    public void setIdCardFee(Double idCardFee) { this.idCardFee = idCardFee; }
    public Double getStationaryFee() { return stationaryFee; }
    public void setStationaryFee(Double stationaryFee) { this.stationaryFee = stationaryFee; }
    public Double getCelebrationFee() { return celebrationFee; }
    public void setCelebrationFee(Double celebrationFee) { this.celebrationFee = celebrationFee; }
    public Double getActivityFee() { return activityFee; }
    public void setActivityFee(Double activityFee) { this.activityFee = activityFee; }
    public Double getYogaMusicDanceFee() { return yogaMusicDanceFee; }
    public void setYogaMusicDanceFee(Double yogaMusicDanceFee) { this.yogaMusicDanceFee = yogaMusicDanceFee; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getTotalChildren() { return totalChildren; }
    public void setTotalChildren(Integer totalChildren) { this.totalChildren = totalChildren; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getChildPhotoPath() { return childPhotoPath; }
    public void setChildPhotoPath(String childPhotoPath) { this.childPhotoPath = childPhotoPath; }
    public String getMotherPhotoPath() { return motherPhotoPath; }
    public void setMotherPhotoPath(String motherPhotoPath) { this.motherPhotoPath = motherPhotoPath; }
    public String getFatherPhotoPath() { return fatherPhotoPath; }
    public void setFatherPhotoPath(String fatherPhotoPath) { this.fatherPhotoPath = fatherPhotoPath; }
    public String getGuardianPhotoPath() { return guardianPhotoPath; }
    public void setGuardianPhotoPath(String guardianPhotoPath) { this.guardianPhotoPath = guardianPhotoPath; }
    public String getBirthCertificatePath() { return birthCertificatePath; }
    public void setBirthCertificatePath(String birthCertificatePath) { this.birthCertificatePath = birthCertificatePath; }
    public String getAadharCardPath() { return aadharCardPath; }
    public void setAadharCardPath(String aadharCardPath) { this.aadharCardPath = aadharCardPath; }
    public String getPanCardPath() { return panCardPath; }
    public void setPanCardPath(String panCardPath) { this.panCardPath = panCardPath; }
}
