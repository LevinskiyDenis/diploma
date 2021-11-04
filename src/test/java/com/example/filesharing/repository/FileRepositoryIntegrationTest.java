//package com.example.filesharing.repository;
//
//import com.example.filesharing.AbstractRepositoryIntegrationTest;
//import com.example.filesharing.entity.File;
//import org.assertj.core.api.Assertions;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.jdbc.Sql;
//
//
//// TODO: этот тест закончил, но надо на свежую голову глянуть, как и остальные интеграционные тесты репозиториев, могло что-то сломаться из-за того, что причесывал контроллеры
//
//@Sql({"/sql/insertUserCredentials.sql","/sql/insertFile.sql"})
//public class FileRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {
//
//    @Autowired
//    FileRepository fileRepository;
//
//    @Test
//    public void getFileByNameEquals(){
//
//        File expectedFile = new File();
//        expectedFile.setName("myimage");
//        expectedFile.setSize(222000);
//        expectedFile.setMimetype("image/png");
//        expectedFile.setFile(new byte[]{49, 50, 51, 52});
//
//        File actualFile = fileRepository.getFileByNameEquals("myimage");
//
//        Assertions.assertThat(actualFile).usingRecursiveComparison().ignoringFields("id", "lastedited", "userCredentials").isEqualTo(expectedFile);
//    }
//
//    @Test
//    public void deleteByNameEquals(){
//
//        fileRepository.deleteByNameEquals("imagetodelete");
//        File imagetodelete = fileRepository.getFileByNameEquals("imagetodelete");
//        Assert.assertNull(imagetodelete);
//
//    }
//
//    @Test
//    public void findByUserCredentialsId(){
//
//        Long expectedNumOfElements = 3L;
//        Long actualNumOfElements = fileRepository.findByUserCredentialsId(1L, PageRequest.ofSize(10)).getTotalElements();
//        Assert.assertEquals(expectedNumOfElements, actualNumOfElements);
//
//    }
//
//
//}
