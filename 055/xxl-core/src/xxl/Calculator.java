package xxl;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.exceptions.InvalidRangeException;
import xxl.exceptions.InvalidSpreadsheetBoundsException;
import xxl.exceptions.DuplicateUserException;
import xxl.exceptions.UnknownFunctionNameException;

import java.util.Map;
import java.util.HashMap;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;

    /** The active user. */
    private User        _activeUser;

    /** The application's users. */
    private Map<String, User> _users = new HashMap<>();


    /**
     * Constructor.
     */
    public Calculator() {
        User root = new User("root");
        _users.put("root", root);
        setActiveUser(root); // Set root as the active user by default
    }

    /**
     * Creates a new User.
     * 
     * @param nameID the user's name.
     * @throws DuplicateUserException if the user nameID already exists.
     */
    public void createUser(String nameID) throws DuplicateUserException {
        User user = new User(nameID);
        if (_users.containsKey(nameID))
            throw new DuplicateUserException();
        _users.put(nameID, user);
    }

    /**
     * Sets a user as the application's active user.
     * 
     * @param user the user to set as the active user.
     */
    public void setActiveUser(User user) {
        _activeUser = user;
    }

    /**
     * @return true if there have been changes made to the spreadsheet.
     */
    public boolean changed() {
        if (_spreadsheet != null)
            return _spreadsheet.hasChanged();
        return false;
    }

    /**
     * @return the application's currently active spreadsheet.
     */
    public Spreadsheet getSpreadsheet() {
	    return _spreadsheet;
    }

    /**
     * Creates a new Spreadsheet.
     * 
     * @param lines number of lines for the spreadsheet.
     * @param columns number of columns for the spreadsheet.
     */
    public void createSpreadsheet(int lines, int columns) 
                throws InvalidSpreadsheetBoundsException {

        if (lines < 1 || columns < 1)
            throw new InvalidSpreadsheetBoundsException();
        _spreadsheet = new Spreadsheet(lines, columns);
        _activeUser.addSpreadsheet(_spreadsheet);
        _spreadsheet.addUser(_activeUser);
    }

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (!changed())
            return;
        if (_spreadsheet.getFilename() == null || _spreadsheet.getFilename().equals(""))
            throw new MissingFileAssociationException();
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(
                                    new FileOutputStream(_spreadsheet.getFilename())))) {
            oos.writeObject(_spreadsheet);
            _spreadsheet.setChanged(false);
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException,
                                        MissingFileAssociationException, IOException {
        _spreadsheet.setFilename(filename);
        save();
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     * 
     */
    public void load(String filename) throws UnavailableFileException {

        try (ObjectInputStream ois = new ObjectInputStream(
            new BufferedInputStream(new FileInputStream(filename)))) {
            
            _spreadsheet = (Spreadsheet) ois.readObject();
            _spreadsheet.setFilename(filename);
            _spreadsheet.setChanged(false);
        }
        catch (FileNotFoundException | ClassNotFoundException e) {
            throw new UnavailableFileException(filename);
        }
        catch (IOException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            if ((line = reader.readLine()) != null) {
                //retrieve the number of lines
                int lines = Integer.parseInt(line.substring(7));
                //retrieve the number of columns
                int columns = Integer.parseInt(reader.readLine().substring(8));
                try {
                    createSpreadsheet(lines, columns);
                }
                // Should never happen
                catch (InvalidSpreadsheetBoundsException e) {
                    e.printStackTrace();
                }
            }
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|");
                if (fields.length > 1)
                    _spreadsheet.insertContents(fields[0], fields[1]);
            }
            
        } catch (IOException | UnrecognizedEntryException | 
                InvalidRangeException | UnknownFunctionNameException e) {

            throw new ImportFileException(filename, e);
        }
    }
}
