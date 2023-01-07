package com.example.project2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;


@ControllerAdvice
public class ExceptionController {
    // log
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // bat cac loai exception tai day

    @ExceptionHandler({ Exception.class })
    public String exception(SQLException ex) {
        // ex.printStackTrace();
        logger.error("sql ex: ", ex);
        return "exception.html";// view
    }

}
