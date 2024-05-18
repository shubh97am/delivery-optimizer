package com.lucidity.deliveryoptimizer.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class InvalidInputRequestException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer code;


    public InvalidInputRequestException() {
    }

    public InvalidInputRequestException(String message) {
        super(message);
    }

    public InvalidInputRequestException(String message, int code) {
        super(message);
        this.code = code;
    }
}
