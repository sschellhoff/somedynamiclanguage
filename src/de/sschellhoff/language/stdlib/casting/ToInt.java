package de.sschellhoff.language.stdlib.casting;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.LangCallable;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.util.List;

public class ToInt implements ExtensionFunction {
    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object argument = arguments.get(0);
        if(argument instanceof Double) {
            return ((Double)argument).intValue();
        }
        if(argument instanceof Boolean) {
            return (Boolean)argument ? 1 : 0;
        }
        if(argument instanceof String) {
            try {
                return Integer.parseInt((String)argument);
            } catch(NumberFormatException nfe) {
                throw new ExtensionRuntimeError("The given string contains no valid int");
            }
        }
        if(argument instanceof Integer) {
            return argument;
        }
        if(argument == null) {
            throw new ExtensionRuntimeError("Cannot cast null to int");
        }
        throw new ExtensionRuntimeError("Invalid cast");
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public String getName() {
        return "int";
    }
}
