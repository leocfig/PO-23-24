package xxl.content;

import java.util.List;
import java.util.ArrayList;

import xxl.Subject;
import xxl.Spreadsheet;
import xxl.cell.Cell;
import xxl.exceptions.InvalidValueException;

/** Class representing a reference to a cell. */
public class Reference extends Content {

    private Spreadsheet _spreadsheet;
    private int  _line;
    private int  _column;

    public Reference(Spreadsheet spreadsheet, int line, int column) {
        _spreadsheet = spreadsheet;
        _line = line;
        _column = column;
        setCurrentValue(evaluate());
    }

    @Override
    public String contentToString(int level) {

        String rendered = _line + ";" + _column;

        if (level == 0) {
            return getCurrentValue().getValue() + "=" + rendered;
        }
        else
            return rendered;
    }

    @Override
    public Content copy() {
        return new Reference(_spreadsheet, _line, _column);
    }

    @Override
    public List<Subject> getDependency() {  // Produce a list of subjects to be observed

        List<Subject> subjects = new ArrayList<Subject>();

        Cell cell = _spreadsheet.getCell(_line, _column);

        if (cell == null) {
            // If the referenced cell has not been allocated
            cell = new Cell(null);
            _spreadsheet.insertCell(_line, _column, cell);
        }
        subjects.add(cell);
        return subjects;
    }

    @Override
    public Result<?> evaluate() {
        try {
            Cell cell = _spreadsheet.getCell(_line, _column);
            // check if the reference is to an empty or inexistent cell.
            if (cell == null || cell.getContent() == null) 
                throw new InvalidValueException();
            return cell.getContent().getCurrentValue();
        }
        catch (InvalidValueException e) {
            return new Result<>(e.getInvalidValue(), Result.Type.STRING);
        }
    }

}