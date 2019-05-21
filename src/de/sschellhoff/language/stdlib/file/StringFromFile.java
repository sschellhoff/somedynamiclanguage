package de.sschellhoff.language.stdlib.file;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;

import java.util.List;

public class StringFromFile implements ExtensionFunction {
    @Override
    public String getName() {
        return "stringfromfile";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object result = (new StringArrayFromFile().call(interpreter, arguments));
        String[] lines = (String[])result;
        return String.join("\n", lines);
    }

    @Override
    public int arity() {
        return 1;
    }
}
