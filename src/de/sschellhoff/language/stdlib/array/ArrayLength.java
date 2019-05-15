package de.sschellhoff.language.stdlib.array;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.util.List;

public class ArrayLength implements ExtensionFunction {
    @Override
    public String getName() {
        return "arraylength";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object argument = arguments.get(0);
        if(argument instanceof Object[]) {
            return ((Object[])argument).length;
        }
        throw new ExtensionRuntimeError("Argument must be of type array");
    }

    @Override
    public int arity() {
        return 1;
    }
}
