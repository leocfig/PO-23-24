package xxl.content.functions;

import xxl.Spreadsheet;
import xxl.cell.Range;
import xxl.cell.Cell;
import xxl.content.Content;
import xxl.content.Result;
import xxl.exceptions.InvalidValueException;


/** Class representing a function that finds the first string in a given range. */
public class Coalesce extends RangeFunction {

    private String _concatenatedString;

    public Coalesce(Range range) {
        super("COALESCE", range); 
    }

    @Override
    public Content copy() {
        return new Coalesce(getRange());
    }

    @Override
    public void initializeVariable() {
        _concatenatedString = "'";
    }

    @Override
    public void calculate(Cell cell) throws InvalidValueException {

        // Check if the cell is empty or inexistent.
        if (!(cell == null || cell.getContent() == null)) {
            Result<?> currentResult = cell.getContent().getCurrentValue();

            // If no string was yet found
            if (_concatenatedString == "'") {

                if (currentResult.getType() == Result.Type.STRING) 
                    _concatenatedString = (String) currentResult.getValue(); // the first string was found
            }
        }
    }

    @Override
    public Result<?> returnResult() {
        return new Result<>(_concatenatedString, Result.Type.STRING);
    }


}