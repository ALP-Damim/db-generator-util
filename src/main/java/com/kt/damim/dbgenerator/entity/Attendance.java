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
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(Attendance.AttendanceId.class)
public class Attendance {
    
    @Id
    @Column(name = "session_id")
    private Integer sessionId;
    
    @Id
    @Column(name = "student_id")
    private Integer studentId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;
    
    private String note;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;  
    
    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }
    
    public static class AttendanceId implements java.io.Serializable {
        private Integer sessionId;
        private Integer studentId;
        
        public AttendanceId() {}
        
        public AttendanceId(Integer sessionId, Integer studentId) {
            this.sessionId = sessionId;
            this.studentId = studentId;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AttendanceId that = (AttendanceId) o;
            return sessionId.equals(that.sessionId) && studentId.equals(that.studentId);
        }
        
        @Override
        public int hashCode() {
            return java.util.Objects.hash(sessionId, studentId);
        }
    }
}
