package xxl;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

/** Class representing a user. Each user has a unique name and 
 *  can have several spreadsheets. */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private String _nameID;
    private List<Spreadsheet> _spreadsheets = new ArrayList<>();
    
    public User(String nameID) {
        _nameID = nameID;
    }

    public String getNameID() {
        return _nameID;
    }

    public void addSpreadsheet(Spreadsheet spreadsheet) {
        _spreadsheets.add(spreadsheet);
    }

}