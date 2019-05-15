package de.sschellhoff.language.extension;

import java.util.Set;

public interface ModuleCollection {
    boolean contains(String moduleName);
    ModuleLoader get(String moduleName);
    Set<String> getModuleNames();
}
