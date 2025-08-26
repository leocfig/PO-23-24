package xxl.content.functions;

import xxl.Spreadsheet;
import xxl.cell.Range;
import xxl.cell.Cell;
import xxl.content.Content;
import xxl.content.Result;
import xxl.exceptions.InvalidValueException;

/** Class representing a function that calculates an average. */
public class Average extends RangeFunction {

    private int _calculatedValue;

    public Average(Range range) {
        super("AVERAGE", range); 
    }

    @Override
    public Content copy() {
        return new Average(getRange());
    }

    @Override
    public void initializeVariable() {
        _calculatedValue = 0;
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
        _calculatedValue += value;
    }

    @Override
    public Result<?> returnResult() {
        _calculatedValue /= super.getRange().rangeLength();
        return new Result<>(_calculatedValue, Result.Type.INT);
    }


}