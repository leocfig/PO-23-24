package xxl.cell;

import xxl.Spreadsheet;
import java.io.Serial;
import java.io.Serializable;

/**
 * Class that represents a range. Ranges can be intervals or a single cell.
 */
public class Range implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private Spreadsheet _spreadsheet;

    private int _startLine;
    private int _startColumn;
    private int _endLine;
    private int _endColumn;

    public Range(Spreadsheet spreadsheet, int startLine, int startColumn,
        int endLine, int endColumn) {
        
        _spreadsheet = spreadsheet;
        _startLine = startLine;
        _startColumn = startColumn;
        _endLine = endLine;
        _endColumn = endColumn;
    }

    public Spreadsheet getSpreadsheet() {
	    return _spreadsheet;
    }

    public int getStartLine() {
        return _startLine;
    }

    public int getStartColumn() {
        return _startColumn;
    }

    public int getEndLine() {
        return _endLine;
    }

    public int getEndColumn() {
        return _endColumn;
    }

    public int rangeLength() {
        if (this.isHorizontal())
            return numColumns();
        return numLines();
    }

    public int numLines() {
        return _endLine - _startLine + 1;
    }

    public int numColumns() {
        return _endColumn - _startColumn + 1;
    }

    public boolean isVertical() {
        return _startColumn == _endColumn;
    }

    public boolean isHorizontal() {
        return _startLine == _endLine;
    }

}