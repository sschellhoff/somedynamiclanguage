package de.sschellhoff.language.stdlib.casting;

import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ModuleLoader;

import java.util.ArrayList;
import java.util.List;

public class Casting implements ModuleLoader {
    @Override
    public String getName() {
        return "cast";
    }

    @Override
    public List<ExtensionFunction> getFunctions() {
        List<ExtensionFunction> functions = new ArrayList<>();
        functions.add(new ToInt());
        return functions;
    }
}
