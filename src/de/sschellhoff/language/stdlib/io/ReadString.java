package de.sschellhoff.language.stdlib.io;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.util.List;
import java.util.Scanner;

public class ReadString implements ExtensionFunction {
    @Override
    public String getName() {
        return "readstring";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object arg = arguments.get(0);
        if(arg != null) {
            if(!(arg instanceof String)) {
                throw new ExtensionRuntimeError("argument must be null or of type string");
            }
            System.out.print(arg.toString());
        }
        Scanner scanner = new Scanner(System.in);

        return scanner.next();
    }

    @Override
    public int arity() {
        return 1;
    }
}
