package xxl.app.visitors;

import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import xxl.Spreadsheet;
import xxl.SearchVisitor;
import xxl.cell.Cell;
import xxl.content.functions.Function;

/** Class for searching functions and rendering the results. */
public class FunctionSearchVisitor implements SearchVisitor {
    
    private String _searchFunction;

    private Spreadsheet _spreadsheet;
    private List<String> _renderedSearch = new ArrayList<>();

    public FunctionSearchVisitor(String searchFunction, Spreadsheet spreadsheet) {
        _searchFunction = searchFunction;
        _spreadsheet = spreadsheet;
    }

    @Override
    public void visitCell(int line, int col) {
        
        Cell cell = _spreadsheet.getCell(line, col);

        if (cell == null || cell.getContent() == null)
            return;

        try {
            Function function = (Function) cell.getContent();
            
            // check if the cell's content matches the search
            if (function.getName().contains(_searchFunction)) {
                
                String unrenderedCell = function.getName() + "|"  + line + "|" + col;
                _renderedSearch.add(unrenderedCell);
            }

        } catch (ClassCastException e) {
            // adds nothing to the search result list.
        }
        
    }

    public List<String> getRenderedSearch() {
        
        Collections.sort(_renderedSearch, functionSearchComparator);

        for (int i = 0; i < _renderedSearch.size(); i++) {

            String[] parts = _renderedSearch.get(i).split("\\|");

            int line = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);

            _renderedSearch.set(i, _spreadsheet.showCell(line, col));
        }
        return _renderedSearch;
    }

    Comparator<String> functionSearchComparator = new Comparator<>() {

        @Override
        public int compare(String string1, String string2) {

            String[] fieldsString1 = string1.split("\\|");
            String[] fieldsString2 = string2.split("\\|");

            int nameComparison = fieldsString1[0].compareTo(fieldsString2[0]);

            if (nameComparison != 0)
                return nameComparison;

            // if the functions have the same name, compare the cell's lines
            int lineComparison = Integer.compare(Integer.parseInt(fieldsString1[1]), 
                                 Integer.parseInt(fieldsString2[1]));

            if (lineComparison != 0)
                return lineComparison;

            // if the lines are the same, compare the cell's columns
            return Integer.compare(Integer.parseInt(fieldsString1[2]), 
                                   Integer.parseInt(fieldsString2[2]));
        }
    };
}