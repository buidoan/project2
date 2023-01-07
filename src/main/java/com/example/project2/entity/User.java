package com.example.project2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "{not.empty}")
    private String name;

    @Column(name = "avatar")
    private String avatar;// URL

    @Column(unique = true)
    @NotBlank(message = "{not.empty}")
    private String username;
    @NotBlank(message = "{not.empty}")
    private String password;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthdate;

    @Transient // field is not persistent.
    private MultipartFile file;

    @CreatedDate // tu gen
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date createdAt;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Student student;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> userRoles;
}
