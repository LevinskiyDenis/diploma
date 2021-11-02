package com.example.filesharing.repository;

import com.example.filesharing.AbstractRepositoryIntegrationTest;
import com.example.filesharing.entity.JwtBlackListEntity;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

public class JwtBlackListRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    JwtBlackListRepository jwtBlackListRepository;

    @Test
    void saveInBlackList() {
        JwtBlackListEntity expected = new JwtBlackListEntity("savedjwt", 10L);

        JwtBlackListEntity actual = jwtBlackListRepository.saveAndFlush(expected);

        Assert.assertEquals(expected, actual);
    }

    @Test
    @Sql("/sql/insertJwtBlackListEntity.sql")
    void findByJwtEquals() {
        Optional<JwtBlackListEntity> expected = Optional.of(new JwtBlackListEntity("abcdedekf", 10L));

        Optional<JwtBlackListEntity> actual = jwtBlackListRepository.findByJwtEquals("abcdedekf");

        Assertions.assertThat(actual).usingRecursiveComparison().ignoringFields("value.id").isEqualTo(expected);
    }

}

