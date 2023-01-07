package com.example.project2.repository;

import com.example.project2.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface TicketRepo extends JpaRepository<Ticket,Integer> {
    @Query("SELECT u FROM Ticket u WHERE u.clientName LIKE :x ")
    Page<Ticket> searchByName(@Param("x") String s, Pageable pageable);

    @Query("SELECT u FROM Ticket u " + "WHERE u.createdAt >= :start and u.createdAt <= :end")
    Page<Ticket> searchByDate(@Param("start") Date start, @Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM Ticket u JOIN u.department d" + " WHERE d.id = :x ")
    Page<Ticket> searchByDepartmentId(@Param("x") int dId, Pageable pageable);

    @Query("SELECT u FROM Ticket u JOIN u.department d" + " WHERE d.name = :x ")
    Page<Ticket> searchByDepartmentName(@Param("x") String dName, Pageable pageable);

    Page<Ticket> findByStatus(boolean status, Pageable pageable);
}
