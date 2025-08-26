package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.exceptions.UnknownFunctionNameException;
import xxl.exceptions.InvalidRangeException;

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        addStringField("range", Prompt.address());
        addStringField("content", Prompt.content());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.insertContents(stringField("range"), stringField("content"));
        }
        catch (UnrecognizedEntryException e) {
            e.printStackTrace();
        }
        catch (UnknownFunctionNameException e) { 
            throw new UnknownFunctionException(e.getName());
        }
        catch (InvalidRangeException e) {
            throw new InvalidCellRangeException(e.getRange());
        }
    }

}
