package com.example.filesharing.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rolename;

    @OneToMany(mappedBy = "role")
    private Set<UserCredentials> userCredentialsSet = new HashSet<>();

    public Role() {
    }

    public Role(Long id, String rolename) {
        this.id = id;
        this.rolename = rolename;
    }

    public String getRolename() {
        return rolename;
    }

    @Override
    public String toString() {
        return "Role{" +
                "rolename='" + rolename + '\'' +
                '}';
    }
}
