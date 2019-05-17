package de.sschellhoff.language.stdlib.ascii;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.util.List;

public class AsciiToInt implements ExtensionFunction {
    @Override
    public String getName() {
        return "asciitoint";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object arg = arguments.get(0);
        if(!(arg instanceof String)) {
            throw new ExtensionRuntimeError("argument must be of type string");
        }
        String str = (String)arg;
        if(str.length() != 1) {
            throw new ExtensionRuntimeError("argument must be of length one");
        }
        return (int)str.charAt(0);
    }

    @Override
    public int arity() {
        return 1;
    }
}
