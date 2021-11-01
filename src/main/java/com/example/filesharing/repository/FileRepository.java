package com.example.filesharing.repository;

import com.example.filesharing.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    File getFileByNameEquals(String filename);

    void deleteByNameEquals(String filename);

    // TODO: сделать работающим параметр лимит + сделать маппинг в ДТО нормально
    @Query("from File f inner join fetch f.userCredentials where f.userCredentials.id = :id")
    Optional<List<File>> findByUserCredentialsId(@Param("id") Long id);

}
