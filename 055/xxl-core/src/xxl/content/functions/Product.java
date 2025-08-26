package xxl.content.functions;

import xxl.Spreadsheet;
import xxl.cell.Range;
import xxl.cell.Cell;
import xxl.content.Content;
import xxl.content.Result;
import xxl.exceptions.InvalidValueException;

/** Class representing a function that calculates the product 
 * of several integer numbers. 
 */
public class Product extends RangeFunction {

    private int _calculatedValue;

    public Product(Range range) {
        super("PRODUCT", range); 
    }

    @Override
    public Content copy() {
        return new Product(getRange());
    }

    @Override
    public void initializeVariable() {
        _calculatedValue = 1;
    }

    @Override
    public void calculate(Cell cell) throws InvalidValueException {

        // Check if the cell is empty or inexistent.
        if (cell == null || cell.getContent() == null) 
            throw new InvalidValueException();

        Result<?> currentResult = cell.getContent().getCurrentValue();

        if (!(currentResult.getType() == Result.Type.INT))   // the value has to be an integer
            throw new InvalidValueException();

        Integer value = (Integer) currentResult.getValue();    // retrieves the value
        _calculatedValue *= value;
    }

    @Override
    public Result<?> returnResult() {
        return new Result<>(_calculatedValue, Result.Type.INT);
    }

    

}