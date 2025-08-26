package xxl;

import java.io.Serial;
import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;

import xxl.SearchVisitor;
import xxl.cell.Cell;
import xxl.cell.Range;
import xxl.content.*;
import xxl.content.functions.*;
import xxl.exceptions.InvalidRangeException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.exceptions.UnknownFunctionNameException;


/**
 * Class representing a spreadsheet. Spreadsheets can be associated with a
 * file name and have several users. 
 * 
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** The Storage Structure. */
    private StorageStructure _storage;

    /** The Spreadsheet's cutbuffer. */
    private StorageStructure _cutbuffer;

    /** The spreadsheet's file name. */
    private String _filename;

    /** Indicates if the spreadsheet has suffered changes. */
    private boolean _changed = false;

    /** The spreadsheet's users. */
    private Map<String, User> _users = new HashMap<>();


    public Spreadsheet(int lines, int columns) {
        _storage = new StorageStructure(lines, columns);
        _cutbuffer = new StorageStructure(0, 0);
    }

    public void addUser(User user) {
        _users.put(user.getNameID(), user);
    }

    public void changed() {
        setChanged(true);
    }

    public boolean hasChanged() {
        return _changed;
    }

    public void setChanged(boolean changed) {
        _changed = changed;
    }

    public void insertCell(int line, int column, Cell cell) {
        _storage.insertCell(line, column, cell);
    }

    /**
     * Insert specified content in specified range.
     *
     * @param rangeSpecification
     * @param contentSpecification
     * @throws UnrecognizedEntryException
     * @throws InvalidRangeException
     * @throws UnknownFunctionNameException
     */
    public void insertContents(String rangeSpecification, String contentSpecification)
        throws UnrecognizedEntryException, InvalidRangeException, UnknownFunctionNameException {
        _storage.insertRange(parseRange(rangeSpecification), parseContent(contentSpecification));
        changed();
    }

    /**
     * Delete the content in specified range.
     *
     * @param rangeSpecification
     * @throws InvalidRangeException
     */
    public void deleteContents(String rangeSpecification) throws InvalidRangeException {
        _storage.deleteRange(parseRange(rangeSpecification));
        changed();
    }

    public String showCell(int line, int column) {
        return _storage.showCell(line, column);
    }

    /**
     * Show specified content in specified range.
     *
     * @param rangeSpecification
     * @throws InvalidRangeException
     */
    public String showContents(String rangeSpecification) throws InvalidRangeException {
        return _storage.showRange(parseRange(rangeSpecification));
    }

    /**
     * Copy the content in specified range.
     *
     * @param rangeSpecification
     * @throws InvalidRangeException
     */
    public void copyContents(String rangeSpecification) throws InvalidRangeException {

        Range range = parseRange(rangeSpecification);
        _cutbuffer = new StorageStructure(range.numLines(), range.numColumns());

        int startLine = range.getStartLine();
        int endLine = range.getEndLine();
        int startColumn = range.getStartColumn();
        int endColumn = range.getEndColumn();

        for (int line = startLine, cutBufferLine = 1; line <= endLine; line++, cutBufferLine++) {
            for (int col = startColumn, cutBufferColumn = 1; col <= endColumn; col++, cutBufferColumn++) {
                Cell cell = _storage.getCell(line, col);

                // if the cell exists and has content
                if (cell != null && cell.getContent() != null) {  
                    Cell copiedCell =  new Cell(cell.getContent().copy());
                    // Make the cutBuffer's cells independent from their source
                    copiedCell.stopObservation();
                    _cutbuffer.insertCell(cutBufferLine, cutBufferColumn, copiedCell);
                }
            }
        }
    }


    /**
     * Paste the cutbuffer's content in specified range.
     *
     * @param rangeSpecification
     * @throws InvalidRangeException
     */
    public void pasteContents(String rangeSpecification) throws InvalidRangeException {

        // if the cutbuffer is empty
        if (_cutbuffer.getNumLines() == 0 || _cutbuffer.getNumColumns() == 0)
            return;

        Range range = parseRange(rangeSpecification);

        // if the range's dimension is different from the cutbuffer's dimension
        // and the range is not a singular cell.
        if (range.rangeLength() != 1 && (range.numLines() != _cutbuffer.getNumLines()
            || range.numColumns() != _cutbuffer.getNumColumns()))
            return;

        int startLine = range.getStartLine();
        int startColumn = range.getStartColumn();

        // also checks if the spreadsheet's limit is not exceeded
        for (int cutBufferLine = 1, line = startLine; 
            cutBufferLine <= _cutbuffer.getNumLines() && line <= getNumLines();
            cutBufferLine++, line++) {
            
            for (int cutBufferColumn = 1, col = startColumn; 
                cutBufferColumn <= _cutbuffer.getNumColumns() && col <= getNumColumns();
                cutBufferColumn++, col++) {

                Cell cell = _cutbuffer.getCell(cutBufferLine, cutBufferColumn);
                if (cell != null) {
                    cell.startObservation();
                    cell.update();
                }
                _storage.insertCell(line, col, cell);
            }
        }
        changed();
    }

    /**
     * Cut the content in specified range.
     *
     * @param rangeSpecification
     * @throws InvalidRangeException
     */
    public void cutContents(String rangeSpecification) throws InvalidRangeException {
        copyContents(rangeSpecification);
        deleteContents(rangeSpecification);
    }

    /**
     * Show the cutbuffer's content.
     *
     */
    public String showCutBuffer() {
        return _cutbuffer.showRange(new Range(this, 1, 1, _cutbuffer.getNumLines(), 
                                                _cutbuffer.getNumColumns()));
    }
    
    
    private Range parseRange(String rangeSpecification)
                                throws InvalidRangeException {

        String[] elements = rangeSpecification.split(";|:");
        
        // check that it has a valid size.
        if (elements.length != 2 && elements.length != 4)
            throw new InvalidRangeException(rangeSpecification);

        int startLine = Integer.parseInt(elements[0]);
        int startColumn = Integer.parseInt(elements[1]);
        
        // in case the range is one cell only.
        int endLine = startLine;
        int endColumn = startColumn;

        if (elements.length == 4) { // the range is an interval.
            endLine = Integer.parseInt(elements[2]);
            endColumn = Integer.parseInt(elements[3]);
        }
        
        if (endLine < startLine) {
            int aux = endLine;
            endLine = startLine;
            startLine = aux;
        } else if (endColumn < startColumn) {
            int aux = endColumn;
            endColumn = startColumn;
            startColumn = aux;
        }

        // the range cannot exceed the limits of the spreadsheet.
        if (startLine < 1 || startColumn < 1 || endLine < 1 || endColumn < 1
            || endLine > getNumLines() || 
            endColumn > getNumColumns()) {

            throw new InvalidRangeException(rangeSpecification);
        }
        // the range has to be either horizontal or vertical.
        if (startLine != endLine && startColumn != endColumn) {
            throw new InvalidRangeException(rangeSpecification);
        }

        return new Range(this, startLine, startColumn, endLine, endColumn);
    }


    private Content parseContent(String contentSpecification) 
                            throws InvalidRangeException, UnrecognizedEntryException,
                            UnknownFunctionNameException {

        // Trim whitespaces
        contentSpecification = contentSpecification.trim();

        // Check if it's an integer (negative or positive)
        if (contentSpecification.matches("\\-?\\d+")) {
            int intValue = Integer.parseInt(contentSpecification);
            Result<Integer> result = new Result<>(intValue, Result.Type.INT);
            return new Literal(result);
        }

        // Check if it's a string
        else if (contentSpecification.startsWith("'")) { 
            
            Result<String> result = new Result<>(contentSpecification, Result.Type.STRING);
            return new Literal(result);
        }

        // Check if it's a reference to another cell
        else if (contentSpecification.matches("=\\d+;\\d+") |
                contentSpecification.matches("\\d+;\\d+")) {

            String rangeSpecification = contentSpecification;
            
            if (contentSpecification.matches("=\\d+;\\d+"))
                // Remove the equal sign
                rangeSpecification = rangeSpecification.substring(1);

            Range singleCell = parseRange(rangeSpecification);
            int referencedLine = singleCell.getStartLine();
            int referencedColumn = singleCell.getStartColumn();
            return new Reference(this, referencedLine, referencedColumn);
        }

        // Check if it's a function
        else if (contentSpecification.startsWith("=")) {

            String functionExpression = contentSpecification.substring(1); // Removes the "="
            return parseFunction(functionExpression);
        }

        else 
            throw new UnrecognizedEntryException(contentSpecification);
    }

    private Function parseFunction(String functionExpression) throws InvalidRangeException,
                        UnrecognizedEntryException, UnknownFunctionNameException {

        // Separate the name and arguments
        String[] elements = functionExpression.split("\\(");

        if (elements.length != 2) {
            throw new UnrecognizedEntryException(functionExpression);
        }

        String functionName = elements[0].trim();
        String argumentString = elements[1].trim();
        
        // Remove the parentheses ")"
        argumentString = argumentString.substring(0, argumentString.length() - 1);

        Content operand1 = null;
        Content operand2 = null;
        Range range = null;

        String[] arguments = argumentString.split(",");
        if (arguments.length == 2) { // If it's a binary function
            operand1 = parseContent(arguments[0]);
            operand2 = parseContent(arguments[1]);
        } else {                     // If it's a range function
            range = parseRange(arguments[0]);
        }
        
        Function function;

        switch (functionName) {
            case "ADD": return new Add(operand1, operand2);
            case "SUB": return new Sub(operand1, operand2);
            case "MUL": return new Mul(operand1, operand2);
            case "DIV": return new Div(operand1, operand2);
            case "AVERAGE": return new Average(range);
            case "PRODUCT": return new Product(range);
            case "CONCAT": return new Concat(range);
            case "COALESCE": return new Coalesce(range);
            default: throw new UnknownFunctionNameException(functionName);
        }
    }

    /**
     * Allows the visitor to visit each cell in the spreadsheet.
     *
     * @param searcher the visitor used to perform the search
     */
    public void accept(SearchVisitor searcher) {
        for (int line = 1; line <= getNumLines(); line++) {
            for (int col = 1; col <= getNumColumns(); col++) {
                searcher.visitCell(line, col);
            }
        }
    }

    public int getNumLines() {
        return _storage.getNumLines();
    }

    public int getNumColumns() {
        return _storage.getNumColumns();
    }

    public Cell getCell(int line, int column) {
        return _storage.getCell(line, column);
    }

    public String getFilename() {
        return _filename;
    }

    public void setFilename(String filename) {
        _filename = filename;
    }

}


