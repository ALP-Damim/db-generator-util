package com.kt.damim.dbgenerator.repository;

import com.kt.damim.dbgenerator.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    
    @Query("SELECT s FROM Session s WHERE s.classId = :classId ORDER BY s.onDate ASC")
    List<Session> findByClassIdOrderByOnDateAsc(@Param("classId") Integer classId);
    
    List<Session> findByOnDateBetween(OffsetDateTime start, OffsetDateTime end);
    
    List<Session> findByOnDate(OffsetDateTime onDate);
}
