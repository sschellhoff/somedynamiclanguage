package de.sschellhoff.language;

import java.util.HashMap;
import java.util.Map;

public class LangInstance {
    private LangClass _class;
    private final Map<String, Object> fields = new HashMap<>();

    LangInstance(LangClass _class) {
        this._class = _class;
    }

    public Object get(Token name) {
        if(fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }
        LangFunction method = _class.findMethod(name.lexeme);
        if(method != null) {
            return method.bind(this);
        }
        throw new RuntimeError(name, "undefined property '" + name.lexeme + "'");
    }

    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    @Override
    public String toString() {
        return "instance(" + _class.name.lexeme + ")";
    }
}
