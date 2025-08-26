package xxl.content.functions;

import xxl.content.Content;

/** Class representing a function. Functions have a name. */
public abstract class Function extends Content {

    private String _name;

    public Function(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}