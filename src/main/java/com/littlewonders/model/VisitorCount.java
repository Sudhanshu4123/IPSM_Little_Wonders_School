package com.littlewonders.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "visitor_count")
public class VisitorCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long count;

    public VisitorCount() {}
    public VisitorCount(Long count) {
        this.count = count;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}
