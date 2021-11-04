package com.example.filesharing.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rolename;

    @OneToMany(mappedBy = "role")
    private Set<UserCredentials> userCredentialsSet = new HashSet<>();

    public Role(Long id, String rolename) {
        this.id = id;
        this.rolename = rolename;
    }
}
