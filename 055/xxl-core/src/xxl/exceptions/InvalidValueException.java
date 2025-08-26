package xxl.exceptions;

import java.io.Serial;

/** Exception thrown when a cell has an invalid value. */
public class InvalidValueException extends Exception {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private static final String INVALID_VALUE = "#VALUE";

    public String getInvalidValue() {
        return INVALID_VALUE;
    }    
}