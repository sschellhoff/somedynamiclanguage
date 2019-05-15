package de.sschellhoff.language.stdlib;

import de.sschellhoff.language.extension.ModuleCollection;
import de.sschellhoff.language.extension.ModuleLoader;
import de.sschellhoff.language.stdlib.casting.Casting;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class StdlibCollection implements ModuleCollection {
    private Map<String, ModuleLoader> modules = new TreeMap<>();

    public StdlibCollection() {
        modules.put("cast", new Casting());
    }
    @Override
    public boolean contains(String moduleName) {
        return modules.containsKey(moduleName);
    }

    @Override
    public ModuleLoader get(String moduleName) {
        return modules.get(moduleName);
    }

    @Override
    public Set<String> getModuleNames() {
        return modules.keySet();
    }
}
