package de.sschellhoff.language.stdlib.string;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.util.List;

public class Substring implements ExtensionFunction {
    @Override
    public java.lang.String getName() {
        return "substring";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object stro = arguments.get(0);
        if(!(stro instanceof String)) {
            throw new ExtensionRuntimeError("first parameter must be of type string");
        }
        Object begino = arguments.get(1);
        Object endo = arguments.get(2);
        if(!(begino instanceof Integer)) {
            throw new ExtensionRuntimeError("second parameter must be of type int");
        }
        if(!(endo instanceof Integer)) {
            throw new ExtensionRuntimeError("third parameter must be of type int");
        }
        String str = (String)stro;
        int begin = (Integer)begino;
        int end = (Integer)endo;
        return str.substring(begin, end);
    }

    @Override
    public int arity() {
        return 3;
    }
}
