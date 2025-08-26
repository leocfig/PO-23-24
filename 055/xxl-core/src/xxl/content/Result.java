package xxl.content;

import java.io.Serial;
import java.io.Serializable;

/** Class representing the result of the evaluation of a content. 
 *  Results have a value and a type. 
 */
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    public enum Type {
        INT, STRING
    }

    private T value;
    private Type type;

    public Result(T value, Type type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    @Override
	public boolean equals(Object o) {
		if (o instanceof Result) {

			Result<?> result = (Result) o;
            return getType() == result.getType() && getValue().equals(result.getValue());
		}
		return false;
	}
}