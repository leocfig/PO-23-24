package xxl.content;

import java.util.List;
import java.util.ArrayList;

import xxl.Subject;


/** Class representing a literal. Literals can be integers or strings. */
public class Literal extends Content {

    public Literal(Result<?> value) {
        setCurrentValue(value);
    }

    @Override
    public String contentToString(int level) {
        return getCurrentValue().getValue().toString();
    }

    @Override
    public List<Subject> getDependency() {  // Produce an empty list, literals have no dependencies
        List<Subject> subjects = new ArrayList<Subject>();
        return subjects;
    }

    @Override
    public Content copy() {
        return new Literal(getCurrentValue());
    }

    @Override
    public Result<?> evaluate() {
        return getCurrentValue();
    }

}