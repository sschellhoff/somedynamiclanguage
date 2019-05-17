package de.sschellhoff.language.stdlib.io;

import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ModuleLoader;

import java.util.ArrayList;
import java.util.List;

public class IO implements ModuleLoader {
    @Override
    public String getName() {
        return "io";
    }

    @Override
    public List<ExtensionFunction> getFunctions() {
        List<ExtensionFunction> functions = new ArrayList<>();
        functions.add(new ReadString());
        functions.add(new Write());
        return functions;
    }
}
