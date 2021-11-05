package com.example.filesharing.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtBlackListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String jwt;

    @Column(nullable = false)
    Long exp;

    public JwtBlackListEntity(String jwt, Long exp) {
        this.jwt = jwt;
        this.exp = exp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtBlackListEntity jwtBlackListEntity = (JwtBlackListEntity) o;
        return jwt.equals(jwtBlackListEntity.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt);
    }
}
