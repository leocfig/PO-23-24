package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import xxl.app.visitors.ValueSearchVisitor;

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
        addStringField("value", Prompt.searchValue());
    }

    @Override
    protected final void execute() {
        ValueSearchVisitor searcher = new ValueSearchVisitor(stringField("value"), _receiver);
        _receiver.accept(searcher);
        _display.popup(searcher.getRenderedSearch());
    }

}
