package xxl;

import xxl.cell.Cell;

/** Visitor interface for searching in the spreadsheet. */
public interface SearchVisitor {
    
    void visitCell(int line, int col);

}
