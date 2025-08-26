package xxl.content.functions;

import java.util.List;
import java.util.ArrayList;

import xxl.Subject;
import xxl.content.Content;
import xxl.content.Result;
import xxl.exceptions.InvalidValueException;

/** Class representing a binary function with two operands. */
public abstract class BinaryFunction extends Function {

    private Content _operand1;
    private Content _operand2;

    public BinaryFunction(String name, Content operand1, Content operand2) {
        super(name);
        _operand1 = operand1;
        _operand2 = operand2;
        setCurrentValue(evaluate());
    }

    public Content getOperand1() {
        return _operand1;
    }

    public Content getOperand2() {
        return _operand2;
    }

    
    @Override
    public List<Subject> getDependency() {  // Produce a list of subjects to be observed
        
        List<Subject> subjects = new ArrayList<Subject>();
        
        // Combine the subjects of both operands
        subjects = _operand1.getDependency();
        subjects.addAll(_operand2.getDependency());

        return subjects;
    }
    

    @Override
    public String contentToString(int level) {

        String rendered = super.getName() + "(" + _operand1.contentToString(level + 1) +
                        "," + _operand2.contentToString(level + 1) + ")";
        if (level == 0)
            return getCurrentValue().getValue() + "=" + rendered;
        else
            return rendered;
    }

    @Override
    public Result<?> evaluate() {

        Result<?> resultOp1 = _operand1.evaluate();   // retrieves the result of operand1, which is a content
        Result<?> resultOp2 = _operand2.evaluate();   // retrieves the result of operand2, which is a content

        try {
            if (!(resultOp1.getType() == Result.Type.INT && 
                resultOp2.getType() == Result.Type.INT)) {      // if one or both operands are not integers

                throw new InvalidValueException();
            }

            Integer value1 = (Integer) resultOp1.getValue();    // retrieves the value of the result of operand1
            Integer value2 = (Integer) resultOp2.getValue();    // retrieves the value of the result of operand2

            Integer resultValue = calculate(value1, value2);    // each subclass calculates accordingly
            return new Result<>(resultValue, Result.Type.INT);
        }
        catch (InvalidValueException e) {
            return new Result<>(e.getInvalidValue(), Result.Type.STRING);
        }
    }

    public abstract int calculate(int value1, int value2) throws InvalidValueException;

}