package de.sschellhoff.language;

import java.util.List;

public interface LangCallable {
    Object call(Interpreter interpreter, List<Object> arguments);

    int arity();
}
