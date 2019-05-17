package de.sschellhoff.language.stdlib.string;

import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ModuleLoader;

import java.util.ArrayList;
import java.util.List;

public class Strings implements ModuleLoader {
    @Override
    public java.lang.String getName() {
        return "strings";
    }

    @Override
    public List<ExtensionFunction> getFunctions() {
        List<ExtensionFunction> functions = new ArrayList<>();
        functions.add(new Substring());
        functions.add(new StringLength());
        return functions;
    }
}
