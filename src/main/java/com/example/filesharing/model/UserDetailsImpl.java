package com.example.filesharing.model;

import com.example.filesharing.entity.UserCredentials;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter

public class UserDetailsImpl implements UserDetails {

    private final UserCredentials userCredentials;

    public UserDetailsImpl(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userCredentials.getAuthorities();
    }

    @Override
    public String getPassword() {
        return userCredentials.getPassword();
    }

    @Override
    public String getUsername() {
        return userCredentials.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userCredentials.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userCredentials.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userCredentials.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userCredentials.isEnabled();
    }
}
