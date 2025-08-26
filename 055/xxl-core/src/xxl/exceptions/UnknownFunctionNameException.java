package xxl.exceptions;

import java.io.Serial;

/** Exception for unknown function names. */
public class UnknownFunctionNameException extends Exception {

    @Serial
	private static final long serialVersionUID = 202308312359L;

    private String _name;

    public UnknownFunctionNameException(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}