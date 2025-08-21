package com.kt.damim.dbgenerator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(Enrollment.EnrollmentId.class)
public class Enrollment {
    
    @Id
    @Column(name = "student_id")
    private Integer studentId;
    
    @Id
    @Column(name = "class_id")
    private Integer classId;
    
    @Column(nullable = false)
    @Builder.Default
    private String status = "ENROLLED";
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    public static class EnrollmentId implements java.io.Serializable {
        private Integer studentId;
        private Integer classId;
        
        public EnrollmentId() {}
        
        public EnrollmentId(Integer studentId, Integer classId) {
            this.studentId = studentId;
            this.classId = classId;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EnrollmentId that = (EnrollmentId) o;
            return studentId.equals(that.studentId) && classId.equals(that.classId);
        }
        
        @Override
        public int hashCode() {
            return java.util.Objects.hash(studentId, classId);
        }
    }
}
