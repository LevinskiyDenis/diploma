package com.example.filesharing.repository;

import com.example.filesharing.AbstractRepositoryIntegrationTest;
import com.example.filesharing.entity.File;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.io.FileNotFoundException;


@Sql({"/sql/insertFile.sql"})
public class FileRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    FileRepository fileRepository;

    @Test
    public void getFileByNameEquals() throws FileNotFoundException {
        File expectedFile = new File();
        expectedFile.setName("myimage");
        expectedFile.setSize(222000);
        expectedFile.setMimetype("image/png");
        expectedFile.setFile(new byte[]{49, 50, 51, 52});

        File actualFile = fileRepository.findFileByNameEquals("myimage").orElseThrow(FileNotFoundException::new);

        Assertions.assertThat(actualFile).usingRecursiveComparison().ignoringFields("id", "lastedited", "userCredentials").isEqualTo(expectedFile);
    }


    @Test
    public void findByUserCredentialsId(){

        Long expectedNumOfElements = 3L;
        Long actualNumOfElements = fileRepository.findFilesByUserCredentialsId(1L, PageRequest.ofSize(10)).getTotalElements();
        Assert.assertEquals(expectedNumOfElements, actualNumOfElements);

    }


}
