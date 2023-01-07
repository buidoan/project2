package com.example.project2.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Integer id;
    // javax.validation
    @NotBlank(message = "{not.empty}")
    @Size(min = 6, max = 20)
    private String name;


    // map form du lieu string sang date
//	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @CreatedDate // tu gen
    private Date createdAt;
}
