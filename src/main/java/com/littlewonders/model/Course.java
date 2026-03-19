package com.littlewonders.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private Double standardFee;
    private String duration; // Like "1 year", "6 months"

    private boolean active = true;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getStandardFee() { return standardFee; }
    public void setStandardFee(Double standardFee) { this.standardFee = standardFee; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
