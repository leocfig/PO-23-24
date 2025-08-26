package xxl.content.functions;

import xxl.content.Content;
import xxl.exceptions.InvalidValueException;

/** Class representing a binary addition function. */
public class Add extends BinaryFunction {

    public Add(Content operand1, Content operand2) {
        super("ADD", operand1, operand2);
    }

    @Override
    public Content copy() {
        Content operand1Copy = getOperand1().copy();
        Content operand2Copy = getOperand2().copy();
        return new Add(operand1Copy, operand2Copy);
    }

    @Override
    public int calculate(int value1, int value2) throws InvalidValueException {
        return value1 + value2;
    }
}  

