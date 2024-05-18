package com.lucidity.deliveryoptimizer.common.mysql.exception;


public class UniqueKeyConstraintException extends RuntimeException {
    private static final long serialVersionUID = 6238603721757718789L;

    public UniqueKeyConstraintException(Throwable cause) {
        super(cause.getMessage());
    }

}
