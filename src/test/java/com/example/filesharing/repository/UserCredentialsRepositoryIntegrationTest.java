package com.example.filesharing.repository;

import com.example.filesharing.AbstractRepositoryIntegrationTest;
import com.example.filesharing.entity.Role;
import com.example.filesharing.entity.UserCredentials;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.Column;
import javax.swing.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

public class UserCredentialsRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    UserCredentialsRepository userCredentialsRepository;

    @Test
    @Sql(scripts = "/sql/insertUserCredentials.sql")
    void findByUsernameEquals() {

        Optional<UserDetails> userDetailsExpected = Optional.of(new UserCredentials(1L, "denis", "$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC",
                true, true, true, true));

        Optional<UserDetails> userDetailsActual = userCredentialsRepository.findByUsernameEquals("denis");

        Assertions.assertThat(userDetailsActual).usingRecursiveComparison().ignoringFieldsOfTypes(Role.class).isEqualTo(userDetailsExpected);
    }

}