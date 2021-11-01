package com.example.filesharing.repository;

import com.example.filesharing.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {

    @Query("select u from UserCredentials u join fetch u.role where u.username = :username")
    Optional<UserDetails> findByUsernameEquals(@Param("username") String username);

    Optional<UserCredentials> findUserCredentialsByUsernameEquals(String username);

}
