package com.example.filesharing.service;

import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.model.UserDetailsImpl;
import com.example.filesharing.repository.UserCredentialsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCredentialsRepository userCredentialsRepository;

    public UserDetailsServiceImpl(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredentials> userCredentialsOptional = userCredentialsRepository.findUserCredentialsWithRoleByUsernameEquals(username);
        UserCredentials userCredentials = userCredentialsOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new UserDetailsImpl(userCredentials);
    }
}
