package com.example.demoapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflictData(Exception ex) {
        logger.info(ex.getMessage());
        System.out.println(ex.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "409");
        map.put("error", "Conflict data");
        return map;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, String> methodNotSupportedException(Exception ex) {
        logger.info(ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("code", "405");
        response.put("error", "Method Not Allow");

        return response;
    }
    
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, String> entityNotFoundException(Exception ex) {
        logger.info(ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("code", "500");
        response.put("error", "Cant find ");

        return response;
    }

    @ExceptionHandler(javax.persistence.NoResultException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> noResult(Exception ex) {
        logger.info(ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("code", "500");
        response.put("error", "Cant find");

        return response;
    }
    
    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, String> emptyResultDataAccessException(Exception ex) {
        logger.info(ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("code", "500");
        response.put("error", "Not exists to delete ");

        return response;
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> success(Exception ex) {
        logger.info(ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("code", "200");
        response.put("error", "Success");

        return response;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequestHandler(Exception ex) {
        logger.info(ex.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("code", "400");
        response.put("error", "Params are wrong types");

        return response;
    }
}
