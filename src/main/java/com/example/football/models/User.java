package com.example.football.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String phone;
    private String email;
    private String image;
    @Transient
    MultipartFile file;
    private java.util.Date created;
    private String status;



}
