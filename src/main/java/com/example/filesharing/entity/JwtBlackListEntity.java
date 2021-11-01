package com.example.filesharing.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class JwtBlackListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String jwt;

    @Column(nullable = false)
    Long exp;

    public JwtBlackListEntity() {
    }

    public JwtBlackListEntity(String jwt, Long exp) {
        this.jwt = jwt;
        this.exp = exp;
    }

    public JwtBlackListEntity(Long id, String jwt, Long exp) {
        this.id = id;
        this.jwt = jwt;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "JwtBlackListEntity{" +
                "id=" + id +
                ", jwt='" + jwt + '\'' +
                ", exp=" + exp +
                '}';
    }
}
