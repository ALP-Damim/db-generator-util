package com.kt.damim.dbgenerator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    
    @Id
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "desired_course")
    private String desiredCourse;
    
    @Column(name = "desired_job")
    private String desiredJob;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    private String school;
    
    private String phone;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
