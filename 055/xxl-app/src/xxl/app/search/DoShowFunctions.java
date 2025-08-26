package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import xxl.app.visitors.FunctionSearchVisitor;

/**
 * Command for searching function names.
 */
class DoShowFunctions extends Command<Spreadsheet> {

    DoShowFunctions(Spreadsheet receiver) {
        super(Label.SEARCH_FUNCTIONS, receiver);
        addStringField("function", Prompt.searchFunction());
    }

    @Override
    protected final void execute() {
        FunctionSearchVisitor searcher = new FunctionSearchVisitor(stringField("function"), _receiver);
        _receiver.accept(searcher);
        _display.popup(searcher.getRenderedSearch());
    }

}
