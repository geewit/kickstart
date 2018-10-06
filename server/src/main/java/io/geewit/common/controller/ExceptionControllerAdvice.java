package io.geewit.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.geewit.core.exception.CustomizedException;

/**
 截取Exception 返回json {@link ControllerAdvice}
 @author geewit
 @since  2017-04-06
 */
@ControllerAdvice(annotations = { RestController.class, Controller.class})
public class ExceptionControllerAdvice {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(CustomizedException.class)
    public ResponseEntity<String> processCustomizedException(CustomizedException e) {
        String message;
        HttpStatus httpStatus;
        if(e != null) {
            logger.info(e.getMessage());
            message = e.getMessage();
            httpStatus = e.getHttpStatus();
        } else {
            message = "unknown";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(httpStatus).body(message); //返回exception.getMessage()
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> processException(Exception e) {
        String message;
        if(e != null) {
            logger.warn(e.getMessage(), e);
            message = e.getMessage();
        } else {
            message = "unknown";
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message); //返回exception.getMessage()
    }
}
