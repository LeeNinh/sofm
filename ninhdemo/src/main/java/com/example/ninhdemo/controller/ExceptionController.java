package com.example.ninhdemo.controller;

import com.example.ninhdemo.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class ExceptionController {
    Logger logger =  LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({org.springframework.validation.BindException.class})
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseDTO sqlError(BindException ex) {
//        ex.printStackTrace();
        logger.error("ERROR: ", ex);
        String msg = ex.getFieldError().getField() + "" + ex.getFieldError().getDefaultMessage();
        return new ResponseDTO(400, msg );
    }
    // bat cac loai exception tai day
    @ExceptionHandler({ Exception.class })
    public String exception(SQLException ex) {
        // ex.printStackTrace();
        logger.error("sql ex: ", ex);
        return "exception.html";// view
    }
}
