package de.sschellhoff.language.stdlib.file;

import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ModuleLoader;

import java.util.ArrayList;
import java.util.List;

public class File implements ModuleLoader {
    @Override
    public String getName() {
        return "file";
    }

    @Override
    public List<ExtensionFunction> getFunctions() {
        List<ExtensionFunction> functions = new ArrayList<>();
        functions.add(new StringFromFile());
        functions.add(new StringArrayFromFile());
        return functions;
    }
}
