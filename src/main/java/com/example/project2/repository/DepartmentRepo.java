package com.example.project2.repository;

import com.example.project2.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface DepartmentRepo extends JpaRepository<Department, Integer> {
    @Query("SELECT u FROM Department u WHERE u.name LIKE :x ")
    Page<Department> searchByName(@Param("x") String s, Pageable pageable);

    @Query("SELECT u FROM Department u " + "WHERE u.createdAt >= :start and u.createdAt <= :end")
    Page<Department> searchByDate(@Param("start") Date start, @Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM Department u " + "WHERE u.createdAt >= :start")
    Page<Department> searchByStartDate(@Param("start") Date start, Pageable pageable);

    @Query("SELECT u FROM Department u " + "WHERE u.createdAt <= :end")
    Page<Department> searchByEndDate(@Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM Department u WHERE u.name LIKE :x AND u.createdAt >= :start AND u.createdAt <= :end")
    Page<Department> searchByNameAndDate(@Param("x") String s, @Param("start") Date start, @Param("end") Date end,
                                         Pageable pageable);

    @Query("SELECT u FROM Department u WHERE u.name LIKE :x AND u.createdAt >= :start")
    Page<Department> searchByNameAndStartDate(@Param("x") String s, @Param("start") Date start, Pageable pageable);

    @Query("SELECT u FROM Department u WHERE u.name LIKE :x AND u.createdAt <= :end")
    Page<Department> searchByNameAndEndDate(@Param("x") String s, @Param("end") Date end, Pageable pageable);

}
