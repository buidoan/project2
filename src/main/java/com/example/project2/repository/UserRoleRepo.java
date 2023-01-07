package com.example.project2.repository;

import com.example.project2.entity.User;
import com.example.project2.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepo extends JpaRepository<UserRole,Integer> {
    @Query("SELECT ur FROM UserRole ur WHERE ur.role LIKE :x ")
    Page<UserRole> searchByName(@Param("x") String role, Pageable pageable);
}
