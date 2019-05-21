package de.sschellhoff.language.stdlib.file;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.Misc;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StringArrayFromFile implements ExtensionFunction {
    @Override
    public String getName() {
        return "stringarrayfromfile";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object arg = arguments.get(0);
        if(!(arg instanceof String)) {
            throw new ExtensionRuntimeError("parameter must be of type string");
        }
        Path path = Misc.toAbsolutePath((String)arg, Misc.getWorkingDirectory());
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.toArray();
        } catch (IOException e) {
            throw new ExtensionRuntimeError("could not read file");
        }
    }

    @Override
    public int arity() {
        return 1;
    }
}
