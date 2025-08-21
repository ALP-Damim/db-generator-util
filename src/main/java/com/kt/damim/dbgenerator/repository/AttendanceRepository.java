package com.kt.damim.dbgenerator.repository;

import com.kt.damim.dbgenerator.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Attendance.AttendanceId> {
    
    @Query("SELECT a FROM Attendance a WHERE a.studentId = :studentId AND a.sessionId = :sessionId")
    List<Attendance> findByStudentIdAndSessionId(@Param("studentId") Integer studentId, @Param("sessionId") Integer sessionId);
    
    // 클래스별 출석 조회는 세션을 통해 조회해야 함
    @Query("SELECT a FROM Attendance a JOIN Session s ON a.sessionId = s.sessionId WHERE a.studentId = :studentId AND s.classId = :classId")
    List<Attendance> findByStudentIdAndClassId(@Param("studentId") Integer studentId, @Param("classId") Integer classId);
    
    List<Attendance> findBySessionId(Integer sessionId);
    
    List<Attendance> findByStudentId(Integer studentId);
    
    List<Attendance> findByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);
}
