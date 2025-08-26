package xxl.content.functions;

import java.util.List;
import java.util.ArrayList;

import xxl.Spreadsheet;
import xxl.Subject;
import xxl.content.Result;
import xxl.cell.Cell; 
import xxl.cell.Range;
import xxl.content.Content;
import xxl.exceptions.InvalidValueException;


/** Class representing a function applicable to ranges. */
public abstract class RangeFunction extends Function {

    private Range _range;

    public RangeFunction(String name, Range range) {
        super(name);
        _range = range;
        setCurrentValue(evaluate());
    }

    public Range getRange() {
        return _range;
    }

    @Override
    public String contentToString(int level) {
        String rendered = super.getName() + "(" 
                + _range.getStartLine() + ";" + _range.getStartColumn() + ":"
                + _range.getEndLine() + ";" + _range.getEndColumn() + ")";
        if (level == 0) 
            return getCurrentValue().getValue() + "=" + rendered;
        else
            return rendered;
    }

    @Override
    public List<Subject> getDependency() {  // Produce a list of subjects to be observed

        List<Subject> subjects = new ArrayList<Subject>();

        int startLine = _range.getStartLine();
        int endLine = _range.getEndLine();
        int startColumn = _range.getStartColumn();
        int endColumn = _range.getEndColumn();
        Spreadsheet spreadsheet = _range.getSpreadsheet();
        
        for (int line = startLine; line <= endLine; line++) {
            for (int col = startColumn; col <= endColumn; col++) {

                Cell cell = spreadsheet.getCell(line, col);

                if (cell == null) {
                    // If the range contains a cell that has not been allocated
                    cell = new Cell(null);
                    spreadsheet.insertCell(line, col, cell);
                }
                subjects.add(cell);
            }
        }
        return subjects;
    }
    
    @Override
    public Result<?> evaluate() {
        
        initializeVariable();

        int startLine = _range.getStartLine();
        int endLine = _range.getEndLine();
        int startColumn = _range.getStartColumn();
        int endColumn = _range.getEndColumn();
        Spreadsheet spreadsheet = _range.getSpreadsheet();

        try {
            for (int line = startLine; line <= endLine; line++) {
                for (int col = startColumn; col <= endColumn; col++) {

                    Cell cell = spreadsheet.getCell(line, col);
                    calculate(cell);
                }
            }
        } 
        catch (InvalidValueException e) {
            return new Result<>(e.getInvalidValue(), Result.Type.STRING);
        }
        return returnResult();
    }

    public abstract void initializeVariable();

    public abstract void calculate(Cell cell) throws InvalidValueException;

    public abstract Result<?> returnResult();



}