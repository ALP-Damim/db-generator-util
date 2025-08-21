package com.kt.damim.dbgenerator.repository;

import com.kt.damim.dbgenerator.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    
    Optional<UserProfile> findByUserId(Integer userId);
}
