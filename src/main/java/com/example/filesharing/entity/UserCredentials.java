package com.example.filesharing.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserCredentials {

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

    public UserCredentials(Long id, String username, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        AccountNonExpired = accountNonExpired;
        AccountNonLocked = accountNonLocked;
        CredentialsNonExpired = credentialsNonExpired;
        Enabled = enabled;
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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRolename()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCredentials userCredentials = (UserCredentials) o;
        return username.equals(userCredentials.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
