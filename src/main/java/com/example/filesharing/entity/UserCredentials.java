package com.example.filesharing.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserCredentials implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "userCredentials", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<File> files = new HashSet<>();

    @Column(columnDefinition = "boolean default true")
    private boolean AccountNonExpired;

    @Column(columnDefinition = "boolean default true")
    private boolean AccountNonLocked;

    @Column(columnDefinition = "boolean default true")
    private boolean CredentialsNonExpired;

    @Column(columnDefinition = "boolean default true")
    private boolean Enabled;

    public UserCredentials() {

    }

    public UserCredentials(Long id, String username, String password, Role role, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        AccountNonExpired = accountNonExpired;
        AccountNonLocked = accountNonLocked;
        CredentialsNonExpired = credentialsNonExpired;
        Enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role.getRolename()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.AccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.AccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.CredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.Enabled;
    }

    public UserCredentials(Long id, String username, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        AccountNonExpired = accountNonExpired;
        AccountNonLocked = accountNonLocked;
        CredentialsNonExpired = credentialsNonExpired;
        Enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
