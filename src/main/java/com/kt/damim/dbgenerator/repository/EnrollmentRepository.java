package com.kt.damim.dbgenerator.repository;

import com.kt.damim.dbgenerator.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Enrollment.EnrollmentId> {
    
    List<Enrollment> findByStudentId(Integer studentId);
    
    List<Enrollment> findByClassId(Integer classId);
    
    List<Enrollment> findByStudentIdAndClassId(Integer studentId, Integer classId);
    
    boolean existsByStudentIdAndClassId(Integer studentId, Integer classId);
}
