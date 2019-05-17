package de.sschellhoff.language.stdlib.string;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.util.List;

public class StringLength implements ExtensionFunction {
    @Override
    public java.lang.String getName() {
        return "stringlength";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object stro = arguments.get(0);
        if(!(stro instanceof String)) {
            throw new ExtensionRuntimeError("Parameter must be of type string");
        }
        String str = (String)stro;
        return str.length();
    }

    @Override
    public int arity() {
        return 1;
    }
}
