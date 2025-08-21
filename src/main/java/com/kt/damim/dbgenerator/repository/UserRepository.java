package com.kt.damim.dbgenerator.repository;

import com.kt.damim.dbgenerator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByRole(User.UserRole role);
    List<User> findByIsActiveTrue();
}
