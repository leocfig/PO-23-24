package xxl.exceptions;

import java.io.Serial;

/** Exception for invalid cell ranges. */
public class InvalidRangeException extends Exception {

    @Serial
	private static final long serialVersionUID = 202308312359L;

    private String _range;

    public InvalidRangeException(String range) {
        _range = range;
    }

    public String getRange() {
        return _range;
    }
}