package xxl;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import xxl.cell.Cell;
import xxl.cell.Range;
import xxl.content.Content;

/** Class representing a storage structure. This structure stores a grid of cells. */
public class StorageStructure implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    /* The spreadsheet's cells - a HashMap with lines as keys and another
    HashMap as the values, that has columns as keys and cells as values */
    private Map<Integer, Map<Integer, Cell>> _cells;

    private int _numLines;
    private int _numColumns;
    
    public StorageStructure(int lines, int columns) {
        _cells = new HashMap<>();
        _numLines = lines;
        _numColumns = columns;
    }

    public void insertCell(int line, int column, Cell cell) {
        if (!_cells.containsKey(line))
            _cells.put(line, new HashMap<>());
        _cells.get(line).put(column, cell);
    }

    public void insertRange(Range range, Content content) {
        int startLine = range.getStartLine();
        int endLine = range.getEndLine();
        int startColumn = range.getStartColumn();
        int endColumn = range.getEndColumn();

        for (int line = startLine; line <= endLine; line++) {
            for (int col = startColumn; col <= endColumn; col++) {

                // Create a new cell with the specified content
                if (getCell(line, col) == null) {
                    Cell cell = new Cell(content);
                    insertCell(line, col, cell);
                }
                // Replace the cell's content
                else {
                    getCell(line, col).setContent(content);
                }
            }
        }
    }   

    public void deleteCellContent(int line, int column) {
            
        Cell cell = getCell(line, column);
        if (cell != null && cell.getContent() != null) // if the cell exists and has content
            cell.setContent(null);
    }


    public void deleteRange(Range range) {
        int startLine = range.getStartLine();
        int endLine = range.getEndLine();
        int startColumn = range.getStartColumn();
        int endColumn = range.getEndColumn();

        for (int line = startLine; line <= endLine; line++) {
            for (int col = startColumn; col <= endColumn; col++) {
                deleteCellContent(line, col);
            }
        }
    } 
    
    public Cell getCell(int line, int column) {
        if (_cells.containsKey(line))        
            return _cells.get(line).get(column);
        return null;
    }

    public String showCell(int line, int column) {

        Cell cell = getCell(line, column);               // Retrieve the cell
        String address = line + ";" + column + "|";

        if (cell != null && cell.getContent() != null) { // If the cell exists and has content
            Content content = cell.getContent();         // Retrieve the content
            return address + content.contentToString(0); // First level
        }
        else                                             // If the cell is empty
            return address;
    }

    public String showRange(Range range) {

        StringBuilder stringToShow = new StringBuilder();

        int startLine = range.getStartLine();
        int endLine = range.getEndLine();
        int startColumn = range.getStartColumn();
        int endColumn = range.getEndColumn();

        for (int i = startLine; i <= endLine; i++) {
            for (int j = startColumn; j <= endColumn; j++) {

                String cellToShow = showCell(i, j);
                stringToShow.append(cellToShow);

                if (j < endColumn)
                    stringToShow.append("\n");
            }
            if (i < endLine) {
                stringToShow.append("\n");
            }
        }
        return stringToShow.toString();
    }

    public int getNumLines() {
        return _numLines;
    }

    public int getNumColumns() {
        return _numColumns;
    }
}






