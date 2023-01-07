package com.example.project2.repository;

import com.example.project2.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScoreRepo extends JpaRepository<Score, Integer> {
    @Query("SELECT s FROM Score s " + " join s.course c " + " join s.student st " + " WHERE  s.score = :score and c.name like :x and st.studentCode like :y")
    Page<Score> searchByScoreAndCourseNameAndStudentCode(@Param("y") String studentCode, @Param("x") String courseName, @Param("score") double score, Pageable pageable);

    @Query("SELECT s FROM Score s " + " WHERE  s.score = :y")
    Page<Score> searchByScore(@Param("y") double score, Pageable pageable);

    @Query("SELECT s FROM Score s " + " join s.course c " + " join s.student st " + " WHERE  c.name like :x ")
    Page<Score> searchByCourseName(@Param("x") String s, Pageable pageable);

    @Query("SELECT s FROM Score s " + " join s.course c " + " join s.student st " + " WHERE   st.studentCode like :y")
    Page<Score> searchByStudentCode(@Param("y") String s, Pageable pageable);

    @Query("SELECT s FROM Score s " + " join s.course c " + " join s.student st " + " WHERE  s.score = :score  and st.studentCode like :y")
    Page<Score> searchByStudentCodeAndScore(@Param("y") String studentCode, @Param("score") double score, Pageable pageable);

    @Query("SELECT s FROM Score s " + " join s.course c " + " join s.student st " + " WHERE  s.score = :score and c.name like :x ")
    Page<Score> searchByCourseNameAndScore(@Param("x") String courseName, @Param("score") double score, Pageable pageable);

    @Query("SELECT s FROM Score s " + " join s.course c " + " join s.student st " + " WHERE   c.name like :x and st.studentCode like :y")
    Page<Score> searchByStudentCodeAndCourseName(@Param("y") String studentCode, @Param("x") String courseName, Pageable pageable);
}
