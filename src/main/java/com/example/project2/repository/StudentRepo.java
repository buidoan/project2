package com.example.project2.repository;

import com.example.project2.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepo extends JpaRepository<Student,Integer> {
   @Query("SELECT s FROM Student s JOIN s.user u" + " WHERE u.username like :x and s.studentCode like :y")
   Page<Student> searchByStudentCodeAndUserName(@Param("y") String studentCode,@Param("x") String userName, Pageable pageable);
   @Query("SELECT s FROM Student s  "+ " WHERE  s.studentCode like :y")
   Page<Student> searchByStudentCode(@Param("y") String studentCode, Pageable pageable);
   @Query("SELECT s FROM Student s JOIN s.user u" + " WHERE  s.studentCode like :y")
   Page<Student> searchByUserName(@Param("y") String studentCode, Pageable pageable);

}
