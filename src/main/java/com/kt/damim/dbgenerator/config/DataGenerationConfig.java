package com.kt.damim.dbgenerator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "data.generation")
public class DataGenerationConfig {
    
    private boolean enabled = false;
    private int teacherCount = 10;
    private int studentCount = 100;
    private int classPerTeacher = 5;
    private int enrollmentPerStudent = 5;
    private int sessionPerClass = 20;
    private double attendanceRate = 0.7; // 70% 출석률
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public int getTeacherCount() {
        return teacherCount;
    }
    
    public void setTeacherCount(int teacherCount) {
        this.teacherCount = teacherCount;
    }
    
    public int getStudentCount() {
        return studentCount;
    }
    
    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
    
    public int getClassPerTeacher() {
        return classPerTeacher;
    }
    
    public void setClassPerTeacher(int classPerTeacher) {
        this.classPerTeacher = classPerTeacher;
    }
    
    public int getEnrollmentPerStudent() {
        return enrollmentPerStudent;
    }
    
    public void setEnrollmentPerStudent(int enrollmentPerStudent) {
        this.enrollmentPerStudent = enrollmentPerStudent;
    }
    
    public int getSessionPerClass() {
        return sessionPerClass;
    }
    
    public void setSessionPerClass(int sessionPerClass) {
        this.sessionPerClass = sessionPerClass;
    }
    
    public double getAttendanceRate() {
        return attendanceRate;
    }
    
    public void setAttendanceRate(double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }
}
