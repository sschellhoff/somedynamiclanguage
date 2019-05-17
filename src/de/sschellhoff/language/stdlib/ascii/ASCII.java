package de.sschellhoff.language.stdlib.ascii;

import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ModuleLoader;

import java.util.ArrayList;
import java.util.List;

public class ASCII implements ModuleLoader {

    @Override
    public String getName() {
        return "ascii";
    }

    @Override
    public List<ExtensionFunction> getFunctions() {
        List<ExtensionFunction> functions = new ArrayList<>();
        functions.add(new IntToAscii());
        functions.add(new AsciiToInt());
        return functions;
    }
}
