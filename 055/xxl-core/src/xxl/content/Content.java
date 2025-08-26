package xxl.content;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

import xxl.cell.Cell;
import xxl.Observer;
import xxl.Subject;


/** Class representing the content that can be found in a cell. */
public abstract class Content implements Serializable {

    private Result<?> _currentValue;

    @Serial
    private static final long serialVersionUID = 202308312359L;

    public Result<?> getCurrentValue() {
        return _currentValue;
    }

    public void setCurrentValue(Result<?> result) {
        _currentValue = result;
    }

    public abstract String contentToString(int level);

    public abstract List<Subject> getDependency();

    public abstract Content copy();
    
    public abstract Result evaluate();
}
