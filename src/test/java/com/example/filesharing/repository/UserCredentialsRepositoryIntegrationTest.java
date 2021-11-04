package com.example.filesharing.repository;

import com.example.filesharing.AbstractRepositoryIntegrationTest;
import com.example.filesharing.entity.Role;
import com.example.filesharing.entity.UserCredentials;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;


public class UserCredentialsRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    UserCredentialsRepository userCredentialsRepository;

    @Test
    @Sql(scripts = "/sql/insertUserCredentials.sql")
    void findByUsernameEquals() {

        Optional<UserDetails> userDetailsExpected = Optional.of(new UserCredentials(1L, "denis", "$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC",
                true, true, true, true));

        Optional<UserDetails> userDetailsActual = userCredentialsRepository.findByUsernameEquals("denis");

        Assertions.assertThat(userDetailsActual).usingRecursiveComparison().ignoringFields("value.id").ignoringFieldsOfTypes(Role.class).isEqualTo(userDetailsExpected);
    }

}