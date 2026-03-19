package com.littlewonders.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "security_audit")
public class SecurityAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String attemptedAt;
    private String status; // FAILED, SUCCESS
    private String ipAddress;
    private String details;

    @PrePersist
    protected void onCreate() {
        attemptedAt = LocalDateTime.now().toString();
    }

    // Manual Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(String attemptedAt) { this.attemptedAt = attemptedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
