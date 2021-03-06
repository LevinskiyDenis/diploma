package com.example.filesharing.repository;

import com.example.filesharing.entity.JwtBlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtBlackListRepository extends JpaRepository<JwtBlackListEntity, Long> {

    Optional<JwtBlackListEntity> findByJwtEquals(String jwt);

}
