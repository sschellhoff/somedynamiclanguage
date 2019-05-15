package de.sschellhoff.language.extension;

import java.util.List;

public interface ModuleLoader {
    String getName();
    List<ExtensionFunction> getFunctions();
}
