package xxl.content.functions;

import xxl.content.Content;
import xxl.exceptions.InvalidValueException;

/** Class representing a binary multiplication function. */
public class Mul extends BinaryFunction {

    public Mul(Content operand1, Content operand2) {
        super("MUL", operand1, operand2); 
    }

    @Override
    public Content copy() {
        Content operand1Copy = getOperand1().copy();
        Content operand2Copy = getOperand2().copy();
        return new Mul(operand1Copy, operand2Copy);
    }

    @Override
    public int calculate(int value1, int value2) throws InvalidValueException {
        return value1 * value2;
    }
}