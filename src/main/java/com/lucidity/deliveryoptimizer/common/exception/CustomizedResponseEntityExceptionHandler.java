package com.lucidity.deliveryoptimizer.common.exception;


import com.lucidity.deliveryoptimizer.domain.response.APIResponse;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Optional;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger("ExceptionHandler");

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response> handleAllExceptions(Exception e, WebRequest request) {
        logger.error(e);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getDescription(false));
        return APIResponse.renderFailure(errorDetails.getMessage(), 101, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(InvalidDataException.class)
    public final ResponseEntity<Response> handleInvalidDataException(InvalidDataException e, WebRequest request) {
        Integer errorCode = Optional.ofNullable(e.getCode()).orElse(101);
        return APIResponse.renderFailure(e.getMessage(), errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputRequestException.class)
    public final ResponseEntity<Response> handleInvalidInputRequestException(InvalidInputRequestException e, WebRequest request) {
        Integer errorCode = Optional.ofNullable(e.getCode()).orElse(101);
        return APIResponse.renderFailure(e.getMessage(), errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistingEntityNotFoundException.class)
    public final ResponseEntity<Response> handleExistingEntityNotFoundException(ExistingEntityNotFoundException e, WebRequest request) {
        Integer errorCode = Optional.ofNullable(e.getCode()).orElse(101);
        return APIResponse.renderFailure(e.getMessage(), errorCode, HttpStatus.NOT_FOUND);
    }
}
