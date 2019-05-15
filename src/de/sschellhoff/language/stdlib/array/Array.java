package de.sschellhoff.language.stdlib.array;

import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ModuleLoader;

import java.util.ArrayList;
import java.util.List;

public class Array implements ModuleLoader {
    @Override
    public String getName() {
        return "array";
    }

    @Override
    public List<ExtensionFunction> getFunctions() {
        List<ExtensionFunction> functions = new ArrayList<>();
        functions.add(new ArrayLength());
        functions.add(new ArrayResize());
        return functions;
    }
}
