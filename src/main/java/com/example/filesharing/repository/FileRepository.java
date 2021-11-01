package com.example.filesharing.repository;

import com.example.filesharing.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    File getFileByNameEquals(String filename);

    void deleteByNameEquals(String filename);

    // TODO: сделать работающим параметр лимит + сделать маппинг в ДТО нормально
    // почитать про пэйджинг и запросы https://stackoverflow.com/questions/21549480/spring-data-fetch-join-with-paging-is-not-working
//    @Query("from File f inner join fetch f.userCredentials where f.userCredentials.id = :id")
//    Optional<List<File>> findByUserCredentialsId(@Param("id") Long id);

    @Query(value = "from File f inner join fetch f.userCredentials where f.userCredentials.id = :id",
            countQuery = "select count(f) from File f where f.userCredentials.id = :id")
    Page<File> findByUserCredentialsId(@Param("id") Long id, Pageable pageable);

}
