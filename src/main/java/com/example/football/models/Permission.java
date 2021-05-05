package com.example.football.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Table(name = "permissions")
public class Permission implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String api;

    @Override
    public int hashCode() {
        return Objects.hashCode(api);
    }
}
