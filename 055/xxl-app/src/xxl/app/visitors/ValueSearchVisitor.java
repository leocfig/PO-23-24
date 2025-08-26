package xxl.app.visitors;

import java.util.List;
import java.util.ArrayList;
import xxl.Spreadsheet;
import xxl.SearchVisitor;
import xxl.cell.Cell;

/** Class for searching values and rendering the results. */
public class ValueSearchVisitor implements SearchVisitor {
    
    private String _searchValue;

    private Spreadsheet _spreadsheet;
    private List<String> _renderedSearch = new ArrayList<>();

    public ValueSearchVisitor(String searchValue, Spreadsheet spreadsheet) {
        _searchValue = searchValue;
        _spreadsheet = spreadsheet;
    }

    @Override
    public void visitCell(int line, int col) {
        
        Cell cell = _spreadsheet.getCell(line, col);
        if (cell == null || cell.getContent() == null)
            return;
        
        // check if the cell's value matches the search
        if (cell.getContent().evaluate().getValue().toString().equals(_searchValue)) {
            
            String cellToShow = _spreadsheet.showCell(line, col);
            _renderedSearch.add(cellToShow);
        }
    }

    public List<String> getRenderedSearch() {
        return _renderedSearch;
    }

}