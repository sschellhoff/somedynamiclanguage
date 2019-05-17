package de.sschellhoff.language.stdlib.io;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;

import java.util.List;

public class Write implements ExtensionFunction {
    @Override
    public String getName() {
        return "write";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object arg = arguments.get(0);
        if(arg == null) {
            System.out.print("null");
        } else {
            System.out.print(arg.toString());
        }
        return null;
    }

    @Override
    public int arity() {
        return 1;
    }
}
