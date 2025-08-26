package xxl.content.functions;

import xxl.content.Content;
import xxl.exceptions.InvalidValueException;

/** Class representing a binary division function. */
public class Div extends BinaryFunction {

    public Div(Content operand1, Content operand2) {
        super("DIV", operand1, operand2); 
    }

    @Override
    public Content copy() {
        Content operand1Copy = getOperand1().copy();
        Content operand2Copy = getOperand2().copy();
        return new Div(operand1Copy, operand2Copy);
    }

    @Override
    public int calculate(int value1, int value2) throws InvalidValueException {
        try {
            return value1 / value2;
        }
        catch (ArithmeticException e) {
            throw new InvalidValueException();
        }
        
    }
}