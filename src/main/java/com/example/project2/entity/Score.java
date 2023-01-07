package com.example.project2.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Data
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(0)
    @Max(10)
    private double score;// diem thi monhoc/nguoi

    @ManyToOne
    private Student student;
    @ManyToOne
    private Course course;
}
