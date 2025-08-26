package xxl.content.functions;

import xxl.Spreadsheet;
import xxl.cell.Range;
import xxl.cell.Cell;
import xxl.content.Content;
import xxl.content.Result;
import xxl.exceptions.InvalidValueException;


/** Class representing a function for concatenating strings. */
public class Concat extends RangeFunction {

    private String _concatenatedString;

    public Concat(Range range) {
        super("CONCAT", range); 
    }

    @Override
    public Content copy() {
        return new Concat(getRange());
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

            if (currentResult.getType() == Result.Type.STRING) { 
                String temp = (String) currentResult.getValue();

                // append the string found after removing the extra apostrophe
                _concatenatedString += temp.substring(1);
            }
        }
    }

    @Override
    public Result<?> returnResult() {
        return new Result<>(_concatenatedString, Result.Type.STRING);
    }


}