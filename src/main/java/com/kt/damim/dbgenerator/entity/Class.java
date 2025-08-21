package com.kt.damim.dbgenerator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer classId;
    
    @Column(name = "teacher_id")
    private Integer teacherId;
    
    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "class_name", nullable = false)
    private String className;
    
    @Column(nullable = false)
    private String semester;
    
    @Column(name = "zoom_url")
    private String zoomUrl;
    
    @Column(name = "held_day")
    private Integer heldDay;
    
    @Column(name = "starts_at")
    private LocalTime startsAt;
    
    @Column(name = "ends_at")
    private LocalTime endsAt;
    
    private Integer capacity;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
    /**
     * held_day 비트셋을 사용하여 요일 정보를 관리
     * 월(1), 화(2), 수(4), 목(8), 금(16), 토(32), 일(64)
     * 예: 월화수 = 1 + 2 + 4 = 7
     */
    public boolean isHeldOnDay(int dayOfWeek) {
        if (heldDay == null) return false;
        return (heldDay & (1 << (dayOfWeek - 1))) != 0;
    }
    
    public void addHeldDay(int dayOfWeek) {
        if (heldDay == null) heldDay = 0;
        heldDay |= (1 << (dayOfWeek - 1));
    }
    
    public void removeHeldDay(int dayOfWeek) {
        if (heldDay != null) {
            heldDay &= ~(1 << (dayOfWeek - 1));
        }
    }
    
    public String getHeldDaysString() {
        if (heldDay == null) return "";
        
        StringBuilder days = new StringBuilder();
        String[] dayNames = {"월", "화", "수", "목", "금", "토", "일"};
        
        for (int i = 0; i < 7; i++) {
            if ((heldDay & (1 << i)) != 0) {
                if (days.length() > 0) days.append(", ");
                days.append(dayNames[i]);
            }
        }
        
        return days.toString();
    }
}
