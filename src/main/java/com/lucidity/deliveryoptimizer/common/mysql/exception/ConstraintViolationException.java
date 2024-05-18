package com.lucidity.deliveryoptimizer.common.mysql.exception;


public class ConstraintViolationException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -1566732133692591072L;

    public ConstraintViolationException(Throwable cause) {
        super(cause.getMessage());
    }

}
