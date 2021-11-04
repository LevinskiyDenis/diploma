package com.example.filesharing.exception;

import org.hibernate.QueryException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.imageio.IIOException;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestControllerAdvice

public class CustomExceptionHandler {

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<BasicExceptionPayload> handleIOException(IOException e) {

        BasicExceptionPayload payload = new BasicExceptionPayload(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );

        return new ResponseEntity<>(payload, payload.getHttpStatus());

    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<BasicExceptionPayload> handleConstraintViolationException(ConstraintViolationException e) {

        String message = "Нарушены констрейнты базы данных. Проверьте параметры вашего запроса";

        BasicExceptionPayload payload = new BasicExceptionPayload(
                message,
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(payload, payload.getHttpStatus());

    }

    @ExceptionHandler(value = {FileNotFoundException.class})
    public ResponseEntity<BasicExceptionPayload> handleFileNotFoundException(FileNotFoundException e) {

        BasicExceptionPayload payload = new BasicExceptionPayload(
                "Файл с таким именем не найден",
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(payload, payload.getHttpStatus());
    }

    @ExceptionHandler(value = {QueryException.class})
    public ResponseEntity<BasicExceptionPayload> handleQueryException(QueryException e) {

        BasicExceptionPayload payload = new BasicExceptionPayload(
                "Неправильные параметры запроса",
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(payload, payload.getHttpStatus());
    }

    @ExceptionHandler(value = {BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<BasicExceptionPayload> handleBadCredentialsAndUsernameNotFoundException(Exception e) {
        BasicExceptionPayload payload = new BasicExceptionPayload(
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(payload, payload.getHttpStatus());
    }

}
