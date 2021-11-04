package com.example.filesharing.service;

import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class UserCredentialsService implements UserDetailsService {

    UserCredentialsRepository userCredentialsRepository;

    @Autowired
    public UserCredentialsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userCredentialsRepository.findByUsernameEquals(s).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public UserCredentials loadUserCredentialsByUsername(String username) throws UsernameNotFoundException {
        return userCredentialsRepository.findUserCredentialsByUsernameEquals(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
