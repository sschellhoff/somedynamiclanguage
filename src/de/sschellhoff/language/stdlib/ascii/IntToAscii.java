package de.sschellhoff.language.stdlib.ascii;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.util.List;

public class IntToAscii implements ExtensionFunction {
    @Override
    public String getName() {
        return "inttoascii";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object arg = arguments.get(0);
        if(!(arg instanceof Integer)) {
            throw new ExtensionRuntimeError("argument must be of type int");
        }
        int i = (Integer)arg;
        return Character.toString((char)i);
    }

    @Override
    public int arity() {
        return 1;
    }
}
