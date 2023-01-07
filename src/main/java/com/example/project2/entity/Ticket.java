package com.example.project2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ticket")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tiketId;

    private String clientName;
    private String clientPhone;

    // .....

    private String content;

    @CreatedDate
//	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date createdAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date processDate;

    private boolean status;

    @ManyToOne
    private Department department;
}
