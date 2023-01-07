package com.example.project2.repository;

import com.example.project2.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepo extends JpaRepository<Course,Integer> {
    @Query("SELECT c FROM Course c JOIN c.studentList s" + " WHERE c.name like :x and s.studentCode like :y")
    Page<Course> searchByCourseNameAndStudentCode(@Param("x") String courseName,@Param("y") String studentCode, Pageable pageable);
    @Query("SELECT c FROM Course c JOIN c.studentList s" + " WHERE  s.studentCode like :y")
    Page<Course> searchByStudentCode(@Param("y")String studentCode, Pageable pageable);
    @Query("SELECT c FROM Course c " + " WHERE  c.name like :y")
    Page<Course> searchByCourseName(@Param("y")String courseName, Pageable pageable);
}
